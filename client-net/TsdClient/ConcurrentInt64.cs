using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace TsdClient
{
    public class ConcurrentInt64 : ConcurrentNumber
    {
        public ConcurrentInt64()
        {

        }
        public ConcurrentInt64(Int64 value)
        {
            Value = value;
        }
        private Int64 value;
        public Int64 Value
        {
            get
            {
                return Interlocked.Read(ref value);
            }
            set
            {
                this.value = value;
            }
        }

        public ConcurrentInt64 Clone()
        {
            return new ConcurrentInt64(Value);
        }

        public void Increment()
        {
            Increment(1);
        }

        public void Increment(ConcurrentInt64 other)
        {
            Value = Interlocked.Add(ref value, other.Value);
        }

        public void Decrement()
        {
            Increment(-1);
        }
        public void Decrement(ConcurrentInt64 other)
        {
            Increment(-other.Value);
        }

        public override string ToString()
        {
            return Value.ToString();
        }
        public static implicit operator ConcurrentInt64(Int64 v)
        {
            return new ConcurrentInt64(v);
        }
        public static implicit operator Int64(ConcurrentInt64 v)
        {
            return v.Value;
        }

        public decimal ToDecimal()
        {
            return new Decimal(Value);
        }
    }
}
