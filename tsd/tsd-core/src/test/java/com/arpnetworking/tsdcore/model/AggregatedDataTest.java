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
package com.arpnetworking.tsdcore.model;

import com.arpnetworking.test.TestBeanFactory;
import com.arpnetworking.tsdcore.statistics.Statistic;
import com.arpnetworking.tsdcore.statistics.TP50Statistic;
import com.arpnetworking.tsdcore.statistics.TP99Statistic;
import com.arpnetworking.utility.test.BuildableEqualsAndHashCodeTester;
import com.google.common.collect.Lists;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests for the AggregatedData class.
 *
 * @author Ville Koskela (vkoskela at groupon dot com)
 */
public class AggregatedDataTest {

    @Test
    public void testBuilder() {
        final Statistic expectedStatistic = new TP99Statistic();
        final String expectedService = "MyService";
        final String expectedHost = "MyHost";
        final String expectedMetric = "MyMetric";
        final double expectedValue = 3.14f;
        final DateTime expectedPeriodStart = new DateTime();
        final Period expectedPeriod = Period.minutes(5);
        final long expectedPopulationSize = 111;
        final List<Quantity> expectedSamples = Lists.newArrayList(TestBeanFactory.createSample(), TestBeanFactory.createSample());

        final AggregatedData aggregatedData = new AggregatedData.Builder()
                .setStatistic(expectedStatistic)
                .setService(expectedService)
                .setHost(expectedHost)
                .setMetric(expectedMetric)
                .setValue(Double.valueOf(expectedValue))
                .setPeriodStart(expectedPeriodStart)
                .setPeriod(expectedPeriod)
                .setPopulationSize(Long.valueOf(expectedPopulationSize))
                .setSamples(expectedSamples)
                .build();

        Assert.assertEquals(expectedStatistic, aggregatedData.getStatistic());
        Assert.assertEquals(expectedHost, aggregatedData.getHost());
        Assert.assertEquals(expectedMetric, aggregatedData.getMetric());
        Assert.assertEquals(expectedValue, aggregatedData.getValue(), 0.001);
        Assert.assertEquals(expectedPeriodStart, aggregatedData.getPeriodStart());
        Assert.assertEquals(expectedPeriod, aggregatedData.getPeriod());
        Assert.assertEquals(expectedPopulationSize, aggregatedData.getPopulationSize());
        Assert.assertEquals(expectedSamples, aggregatedData.getSamples());
    }

    @Test
    public void testEqualsAndHashCode() {
        BuildableEqualsAndHashCodeTester.assertEqualsAndHashCode(
                new AggregatedData.Builder()
                        .setStatistic(new TP99Statistic())
                        .setService("MyServiceA")
                        .setHost("MyHostA")
                        .setMetric("MyMetricA")
                        .setValue(Double.valueOf(3.14f))
                        .setPeriodStart(new DateTime())
                        .setPeriod(Period.minutes(1))
                        .setPopulationSize(Long.valueOf(1))
                        .setSamples(Lists.newArrayList(TestBeanFactory.createSample())),
                new AggregatedData.Builder()
                        .setStatistic(new TP50Statistic())
                        .setService("MyServiceB")
                        .setHost("MyHostB")
                        .setMetric("MyMetricB")
                        .setValue(Double.valueOf(6.28f))
                        .setPeriodStart(new DateTime().plusDays(1))
                        .setPeriod(Period.minutes(5))
                        .setPopulationSize(Long.valueOf(2))
                        .setSamples(Lists.newArrayList(TestBeanFactory.createSample(), TestBeanFactory.createSample())));
    }

    @Test
    public void testToString() {
        final String asString = new AggregatedData.Builder()
                .setStatistic(new TP99Statistic())
                .setService("MyServiceA")
                .setHost("MyHostA")
                .setMetric("MyMetricA")
                .setValue(Double.valueOf(3.14f))
                .setPeriodStart(new DateTime())
                .setPeriod(Period.minutes(1))
                .setPopulationSize(Long.valueOf(1))
                .setSamples(Lists.newArrayList(TestBeanFactory.createSample()))
                .build()
                .toString();
        Assert.assertNotNull(asString);
        Assert.assertFalse(asString.isEmpty());
    }
}
