using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class MetricsFactoryBuilder
    {
        private IList<ISink> Sinks { get; set; }
        private string Host { get; set; }
        private string Service { get; set; }
        private string Cluster { get; set; }
        public MetricsFactoryBuilder(string service, string cluster)
        {
            Sinks = new List<ISink>();
            Host = System.Environment.MachineName;
            SetService(service);
            SetCluster(cluster);
        }

        public MetricsFactoryBuilder SetHost(string host)
        {
            if (host == null)
                throw new InvalidOperationException("Host can't be null");
            Host = host;
            return this;
        }

        public MetricsFactoryBuilder SetService(string service)
        {
            if(service == null)
                throw new InvalidOperationException("Service can't be null");
            Service = service;
            return this;
        }

        public MetricsFactoryBuilder SetCluster(string cluster)
        {
            if(cluster == null)
                throw new InvalidOperationException("Cluster can't be null");
            Cluster = cluster;
            return this;
        }

        public MetricsFactoryBuilder AddSinks(params ISink[] sinks)
        {
            foreach (var x in sinks)
                Sinks.Add(x);
            return this;
        }

        public MetricsFactoryBuilder AddSink(ISink sink)
        {
            Sinks.Add(sink);
            return this;
        }

        public MetricsFactory Build()
        {
            return new MetricsFactory(Host, Service, Cluster, Sinks);
        }
    }
}
