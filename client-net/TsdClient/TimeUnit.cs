using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class TimeUnit : ImmediateUnit
    {
        public TimeUnit(string name, Scale scale=null):
            base(name, scale)
        {

        }
    }
}
