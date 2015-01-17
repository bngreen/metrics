using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient.JSONLogFormat
{
    public class JsonLogSerializerFactory
    {
        public static Versions LatestVersion { get { return Versions.Version2F; } }

        public static IJsonLogSerializer CreateSerializer()
        {
            return CreateSerializer(LatestVersion);
        }
        public static IJsonLogSerializer CreateSerializer(Versions version)
        {
            switch (version)
            {
                case Versions.Version2F:
                    return new TsdClient.JSONLogFormat.Version2F.JsonLogSerializer();
                default:
                    throw new InvalidOperationException("Invalid Version");
            }
        }
    }
}
