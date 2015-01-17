using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class CompoundUnit : IUnit
    {
        public string Name { get; private set; }
        public CompoundUnit(string name)
        {
            Name = name;
            NumeratorUnits = new List<ImmediateUnit>();
            DenominatorUnits = new List<ImmediateUnit>();
        }
        public CompoundUnit AddNumeratorUnit(ImmediateUnit unit)
        {
            NumeratorUnits.Add(unit);
            return this;
        }
        public CompoundUnit AddDenominatorUnit(ImmediateUnit unit)
        {
            DenominatorUnits.Add(unit);
            return this;
        }
        public IList<ImmediateUnit> NumeratorUnits { get; private set; }
        public IList<ImmediateUnit> DenominatorUnits { get; private set; }
    }
}
