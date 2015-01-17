using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class ConcurrentFloat64
    {
        public ConcurrentFloat64()
        {

        }
        public ConcurrentFloat64(Double val)
        {
            Value = val;
        }

        private Double value;
        public Double Value
        {
            get
            {
                lock (this)
                {
                    return value;
                }
            }
            set
            {
                lock (this)
                {
                    this.value = value;
                }
            }
        }
        public void Increment()
        {
            Increment(1);
        }
        public void Increment(ConcurrentFloat64 other)
        {
            lock (this)
            {
                value += other.Value;
            }
        }
        public void Decrement()
        {
            Increment(-1);
        }
        public void Decrement(ConcurrentFloat64 other)
        {
            Increment(-other.Value);
        }

        public static implicit operator ConcurrentFloat64(Double number)
        {
            return new ConcurrentFloat64(number);
        }
        public static implicit operator Double(ConcurrentFloat64 number)
        {
            return number.Value;
        }


        public decimal ToDecimal()
        {
            return new Decimal(Value);
        }
    }
}
