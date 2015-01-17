using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class Sample
    {
        public Sample(
            IDictionary<string, string> annotations,
            IDictionary<string, IEnumerable<IQuantity<ConcurrentInt64>>> timerSamples,
            IDictionary<string, IEnumerable<IQuantity<ConcurrentInt64>>> counterSamples,
            IDictionary<string, IEnumerable<IQuantity<Decimal>>> gaugesSamples)
        {
            Annotations = annotations;
            TimerSamples = timerSamples;
            CounterSamples = counterSamples;
            GaugesSamples = gaugesSamples;
        }
        public IDictionary<string, string> Annotations { get; private set; }
        public IDictionary<string, IEnumerable<IQuantity<ConcurrentInt64>>> TimerSamples { get; private set; }
        public IDictionary<string, IEnumerable<IQuantity<ConcurrentInt64>>> CounterSamples { get; private set; }
        public IDictionary<string, IEnumerable<IQuantity<Decimal>>> GaugesSamples { get; private set; }
    }
}
