using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class ImmediateUnit : IUnit
    {
        public ImmediateUnit()
            : this("", null)
        {
        }
        public string Name { get; private set; }
        public Scale Scale { get; private set; }
        public ImmediateUnit(string name, Scale scale = null)
        {
            Name = name;
            Scale = scale;
        }

        public override string ToString()
        {
            return Name;
        }
    }
}
