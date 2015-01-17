using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class ConcurrentDecimal
    {
        public ConcurrentDecimal()
        {

        }
        public ConcurrentDecimal(Decimal value)
        {
            Value = value;
        }

        private Decimal value;
        public Decimal Value
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
        public void Increment(ConcurrentDecimal other)
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
        public void Decrement(ConcurrentDecimal other)
        {
            Increment(-other.Value);
        }

        public static implicit operator ConcurrentDecimal(Decimal number)
        {
            return new ConcurrentDecimal(number);
        }
        public static implicit operator Decimal(ConcurrentDecimal number)
        {
            return number.Value;
        }
        public static explicit operator ConcurrentDecimal(ConcurrentInt64 number)
        {
            return new ConcurrentDecimal(number.Value);
        }
        public static explicit operator ConcurrentInt64(ConcurrentDecimal number)
        {
            return new ConcurrentInt64((Int64)number.Value);
        }
        public override string ToString()
        {
            return Value.ToString();
        }


        public decimal ToDecimal()
        {
            return Value;
        }
    }
}
