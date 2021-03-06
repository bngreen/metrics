/**
 * Copyright 2014 Groupon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arpnetworking.tsdaggregator.sources;

import com.arpnetworking.tsdaggregator.model.DefaultMetric;
import com.arpnetworking.tsdaggregator.model.DefaultRecord;
import com.arpnetworking.tsdaggregator.model.Metric;
import com.arpnetworking.tsdaggregator.model.MetricType;
import com.arpnetworking.tsdaggregator.model.Record;
import com.arpnetworking.tsdcore.model.Quantity;
import com.arpnetworking.tsdcore.sources.BaseSource;
import com.arpnetworking.tsdcore.sources.Source;
import com.arpnetworking.utility.observer.Observable;
import com.arpnetworking.utility.observer.Observer;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.oval.constraint.NotNull;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of <code>Source</code> which wraps another <code>Source</code>
 * and merges <code>Metric</code> instances within each <code>Record</code>
 * together if the name matches a regular expression with a new name generated
 * through replacement of all matches in the original name.
 *
 * @author Ville Koskela (vkoskela at groupon dot com)
 */
public final class MappingSource extends BaseSource {

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        _source.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        _source.stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("Source", _source)
                .toString();
    }

    private MappingSource(final Builder builder) {
        super(builder);
        _source = builder._source;

        _findAndReplace = Maps.newHashMapWithExpectedSize(builder._findAndReplace.size());
        for (final Map.Entry<String, ? extends List<String>> entry : builder._findAndReplace.entrySet()) {
            _findAndReplace.put(Pattern.compile(entry.getKey()), ImmutableList.<String>copyOf(entry.getValue()));
        }

        _source.attach(new MappingObserver(this, _findAndReplace));
    }

    private final Source _source;
    private final Map<Pattern, List<String>> _findAndReplace;

    private static final Logger LOGGER = Logger.getLogger(MappingSource.class);

    // NOTE: Package private for testing
    /* package private */static final class MappingObserver implements Observer {

        public MappingObserver(final Source source, final Map<Pattern, List<String>> findAndReplace) {
            _source = source;
            _findAndReplace = findAndReplace;
        }

        @Override
        public void notify(final Observable observable, final Object event) {
            if (!(event instanceof Record)) {
                LOGGER.error(String.format("Observed unsupported event; event=%s", event));
                return;
            }

            // Merge the metrics in the record together
            final Record record = (Record) event;
            final Map<String, MergingMetric> mergedMetrics = Maps.newHashMap();
            for (final Map.Entry<String, ? extends Metric> metric : record.getMetrics().entrySet()) {
                boolean found = false;
                for (final Map.Entry<Pattern, List<String>> findAndReplace : _findAndReplace.entrySet()) {
                    final Matcher matcher = findAndReplace.getKey().matcher(metric.getKey());
                    if (matcher.find()) {
                        for (final String replacement : findAndReplace.getValue()) {
                            merge(metric.getValue(), matcher.replaceAll(replacement), mergedMetrics);
                        }
                        found = true;
                    }
                }
                if (!found) {
                    merge(metric.getValue(), metric.getKey(), mergedMetrics);
                }
            }

            // Raise the merged record event with this source's observers
            // NOTE: Do not leak instances of MergingMetric since it is mutable
            _source.notify(
                    _source,
                    new DefaultRecord.Builder()
                            .setMetrics(
                                    Maps.transformEntries(mergedMetrics, new Maps.EntryTransformer<String, MergingMetric, Metric>() {
                                        @Override
                                        public Metric transformEntry(final String key, final MergingMetric mergingMetric) {
                                            return new DefaultMetric.Builder(mergingMetric).build();
                                        }
                                    }))
                            .setTime(record.getTime())
                            .setAnnotations(record.getAnnotations())
                            .build());
        }

        private void merge(final Metric metric, final String key, final Map<String, MergingMetric> mergedMetrics) {
            final MergingMetric mergedMetric = mergedMetrics.get(key);
            if (mergedMetric == null) {
                // This is the first time this metric is being merged into
                mergedMetrics.put(key, new MergingMetric(metric));
            } else if (!mergedMetric.isMergable(metric)) {
                // This instance of the metric is not mergable with previous
                LOGGER.error(String.format("Discarding metric, failed to merge; metric=%s mergedMetric=%s", metric, mergedMetric));
            } else {
                // Merge the new instance in
                mergedMetric.merge(metric);
            }
        }

        private final Source _source;
        private final Map<Pattern, List<String>> _findAndReplace;
    }

    // NOTE: Package private for testing
    /* package private */static final class MergingMetric implements Metric {

        public MergingMetric(final Metric metric) {
            _type = metric.getType();
            _values = Lists.newArrayList(metric.getValues());
        }

        public boolean isMergable(final Metric metric) {
            return _type.equals(metric.getType());
        }

        public void merge(final Metric metric) {
            if (!isMergable(metric)) {
                throw new IllegalArgumentException(String.format("Metric cannot be merged; metric=%s", metric));
            }
            _values.addAll(metric.getValues());
        }

        @Override
        public MetricType getType() {
            return _type;
        }

        @Override
        public List<Quantity> getValues() {
            return Collections.unmodifiableList(_values);
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("Type", _type)
                    .add("Values", _values)
                    .toString();
        }

        private final MetricType _type;
        private final List<Quantity> _values;
    }

    /**
     * Implementation of builder pattern for <code>FileSource</code>.
     *
     * @author Ville Koskela (vkoskela at groupon dot com)
     */
    public static final class Builder extends BaseSource.Builder<Builder> {

        /**
         * Public constructor.
         */
        public Builder() {
            super(MappingSource.class);
        }

        /**
         * Sets the underlying source. Cannot be null.
         * 
         * @param value The underlying source.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setSource(final Source value) {
            _source = value;
            return this;
        }

        /**
         * Sets find and replace expression map. Cannot be null.
         * 
         * @param value The find and replace expression map.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setFindAndReplace(final Map<String, ? extends List<String>> value) {
            _findAndReplace = value;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Builder self() {
            return this;
        }

        @NotNull
        private Source _source;
        @NotNull
        private Map<String, ? extends List<String>> _findAndReplace;
    }
}
