using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient.JSONLogFormat
{
    public interface IJsonLogSerializer
    {
        string Serialize(Sample sample);
    }
}
