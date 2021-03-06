/**
 * Copyright 2014 Brandon Arp
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
package com.arpnetworking.tsdcore.sinks;

import com.arpnetworking.tsdcore.model.AggregatedData;
import com.google.common.base.Objects;

import net.sf.oval.constraint.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * A publisher that wraps multiple others and publishes to all of them. This 
 * class is thread safe.
 * 
 * TODO(vkoskela): Support concurent execution [MAI-98]
 *
 * @author Brandon Arp (barp at groupon dot com)
 */
public final class MultiSink extends BaseSink {

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordAggregateData(final List<AggregatedData> data) {
        LOGGER.debug(getName() + ": Writing aggregated data; size=" + data.size());

        for (final Sink sink : _sinks) {
            sink.recordAggregateData(data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        LOGGER.info(getName() + ": Closing sink");
        for (final Sink sink : _sinks) {
            sink.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("Sinks", _sinks)
                .toString();
    }

    private MultiSink(final Builder builder) {
        super(builder);
        _sinks = builder._sinks;
    }

    private final Collection<Sink> _sinks;

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiSink.class);

    /**
     * Implementation of builder pattern for <code>MultiSink</code>.
     *
     * @author Ville Koskela (vkoskela at groupon dot com)
     */
    public static final class Builder extends BaseSink.Builder<Builder> {

        /**
         * Public constructor.
         */
        public Builder() {
            super(MultiSink.class);
        }

        /**
         * The aggregated data sinks to wrap. Cannot be null.
         * 
         * @param value The aggregated data sinks to wrap.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setSinks(final Collection<Sink> value) {
            _sinks = value;
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
        private Collection<Sink> _sinks;
    }
}
