using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Common.Logging;

namespace TsdClient
{
    internal class TsdCounter : Int64Quantity, ICounter
    {
        private volatile Boolean isOpen;
        private Boolean IsOpen { get { return isOpen; } set { isOpen = value; } }

        private ILog Log { get; set; }

        public string Name { get; private set; }
        public ConcurrentInt64 InitialValue { get; private set; }
        public TsdCounter(string name, ConcurrentInt64 value, IUnit unit, bool isOpen)
            : base(value.Clone(), unit)
        {
            Log = LogManager.GetLogger(typeof(TsdCounter));
            IsOpen = isOpen;
            Name = name;
            InitialValue = value.Clone();
        }
        public void Increment()
        {
            Increment(1);
        }
        public void Increment(ConcurrentInt64 value)
        {
            if (!IsOpen)
            {
                Log.Error(m=>m("Trying to operate a closed counter: {0}", Name));
            }
            Value.Increment(value);
        }
        public void Decrement()
        {
            Decrement(1);
        }
        public void Decrement(ConcurrentInt64 value)
        {
            Value.Decrement(value);
        }

        public override string ToString()
        {
            return "Counter " + Name + ": " + Value;
        }

    }
}
