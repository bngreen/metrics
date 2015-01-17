using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class Float64Quantity : Quantity<ConcurrentFloat64>
    {
        public Float64Quantity(ConcurrentFloat64 value, IUnit unit)
            : base(unit)
        {
            Value = value;
        }
        public override ConcurrentFloat64 Value
        {
            get;
            protected set;
        }
    }
}
