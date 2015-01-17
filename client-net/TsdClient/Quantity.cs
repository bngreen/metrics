using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class Quantity<T> : IQuantity<T>
    {
        public Quantity(IUnit unit)
        {
            Unit = unit;
        }
        public Quantity(T value, IUnit unit)
            : this(unit)
        {
            Value = value;
        }
        public IUnit Unit { get; protected set; }

        public virtual T Value { get; protected set; }
    }
}
