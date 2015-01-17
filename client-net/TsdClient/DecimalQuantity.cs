using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class DecimalQuantity : Quantity<Decimal>
    {
        public DecimalQuantity(Decimal value, IUnit unit)
            : base(unit)
        {
            Value = value;
        }
        public override Decimal Value { get; protected set; }
    }
}
