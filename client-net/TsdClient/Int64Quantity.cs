using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class Int64Quantity : Quantity<ConcurrentInt64>
    {
        public Int64Quantity(ConcurrentInt64 value, IUnit unit):base(unit)
        {
            Value = value;
        }
        public override ConcurrentInt64 Value { get; protected set; }
    }
}
