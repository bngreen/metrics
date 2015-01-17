using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public interface ICounter : IQuantity<ConcurrentInt64>
    {
        /**
         * Increment the counter sample by 1.
         */
        void Increment();

        /**
         * Decrement the counter sample by 1.
         */
        void Decrement();

        /**
         * Increment the counter sample by the specified value.
         * 
         * @param value The value to increment the counter by.
         */
        void Increment(ConcurrentInt64 value);

        /**
         * Decrement the counter sample by the specified value.
         * 
         * @param value The value to decrement the counter by.
         */
        void Decrement(ConcurrentInt64 value);

        ConcurrentInt64 InitialValue { get; }
    }
}
