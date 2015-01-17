using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Common.Logging;

namespace TsdClient
{
    internal sealed class TsdTimer : ITimer
    {
        private volatile Boolean isOpen;
        private Boolean IsOpen { get { return isOpen; } set { isOpen = value; } }

        private ILog Log { get; set; }
        public string Name { get; private set; }
        public TsdTimer(string name, bool isOpen)
        {
            Log = LogManager.GetLogger(typeof(TsdTimer));
            IsOpen = isOpen;
            Name = name;
            Unit = Units.NanoSecond;
            Start();
        }
        public TsdTimer(string name, Int64 elapsed, bool isOpen)
            : this(name, isOpen)
        {
            this.elapsed = elapsed;
        }
        public Int64 StartTime { get { return Interlocked.Read(ref startTime); } }

        private Int64 startTime;
        private Int64 elapsed;


        public Int64 Elapsed
        {
            get
            {
                if (!IsStopped)
                    Log.Error(m => m("Trying to read a non stopped timer: {0}", Name));
                return Interlocked.Read(ref elapsed);
            }
        }

        private IUnit unit;

        public IUnit Unit
        {
            get
            {
                lock (this)
                    return unit;
            }
            set
            {
                lock (this)
                    unit = value;
            }
        }

        private void Start()
        {
            // if(IsStarted)
            //    throw new InvalidOperationException("Trying to start an already started timer: " + Name);
            startTime = DateTime.Now.Ticks / 100;
            IsStopped = false;
        }

        public Boolean IsStarted
        {
            get
            {
                return !IsStopped;
            }
        }

        public void Stop()
        {
            if (!IsOpen)
                Log.Error(m => m("Trying to stop a closed timer: {0}", Name));
            else if (IsStopped)
                Log.Error(m => m("Trying to stop an already stopped timer: {0}", Name));
            else
            {
                elapsed = DateTime.Now.Ticks / 100 - StartTime;
                IsStopped = true;
            }
        }

        private volatile Boolean isStopped;
        public Boolean IsStopped
        {
            get { return isStopped; }
            private set { isStopped = value; }
        }

        public void Dispose()
        {
            if (!IsStopped)
                Stop();
        }
        public override string ToString()
        {
            if (IsStopped)
                return String.Format("Timer: {0} StartTime: {1} Elapsed: {2}", Name, StartTime, Elapsed);
            return String.Format("Timer: {0} Running StartTime: {1}", Name, StartTime);
        }

        public ConcurrentInt64 Value
        {
            get { return Elapsed; }
        }
    }
}
