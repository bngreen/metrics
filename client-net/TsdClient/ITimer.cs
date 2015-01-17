using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public interface ITimer : IDisposable, IQuantity<ConcurrentInt64>
    {
        string Name { get; }


        /**
     * Stop the timer and record timing data in the associated 
     * <code>Metrics</code> instance.
     */
        void Stop();
        //void Start();
        /**
         * Accessor to determine if this <code>Timer</code> instance is running or
         * stopped. Until stopped it does not produce a sample.
         * 
         * @return True if and only if this <code>Timer</code> instance is stopped.
         */
        Boolean IsStopped { get; }
        Boolean IsStarted { get; }

        //TimeUnit Unit { get; set; }
    }
}
