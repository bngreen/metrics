using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class MetricsFactory
    {
        public IEnumerable<ISink> Sinks { get; private set; }
        public string Service { get; private set; }
        public string Host { get; private set; }
        public string Cluster { get; private set; }

        internal MetricsFactory(string host, string service, string cluster, IEnumerable<ISink> sinks)
        {
            Sinks = sinks;
            Service = service;
            Cluster = cluster;
            Host = host;
        }

        public IMetrics CreateMetric()
        {
            return new TsdMetrics(Sinks, Service, Cluster, Host);
        }
    }
}
