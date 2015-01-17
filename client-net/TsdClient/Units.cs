using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public class Units
    {
        public static TimeUnit NanoSecond { get; protected set; }
        public static TimeUnit MicroSecond { get; protected set; }
        public static TimeUnit MilliSecond { get; protected set; }
        public static TimeUnit Second { get; protected set; }
        public static TimeUnit Minute { get; protected set; }
        public static TimeUnit Hour { get; protected set; }
        public static TimeUnit Day { get; protected set; }
        public static TimeUnit Week { get; protected set; }
        public static TimeUnit Month { get; protected set; }
        public static ImmediateUnit Byte { get; protected set; }
        public static ImmediateUnit KiloByte { get; protected set; }
        public static ImmediateUnit MegaByte { get; protected set; }
        public static ImmediateUnit GigaByte { get; protected set; }
        public static ImmediateUnit TeraByte { get; protected set; }
        public static ImmediateUnit PetaByte { get; protected set; }
        public static ImmediateUnit Bit { get; protected set; }
        public static ImmediateUnit Degree { get; protected set; }
        public static ImmediateUnit Radian { get; protected set; }
        public static ImmediateUnit Celcius { get; protected set; }
        public static ImmediateUnit Fahrenheit { get; protected set; }
        public static ImmediateUnit Kelvin { get; protected set; }

        static Units()
        {
            NanoSecond = new TimeUnit("second", Scales.Nano);
            MicroSecond = new TimeUnit("second", Scales.Micro);
            MilliSecond = new TimeUnit("second", Scales.Milli);
            Minute = new TimeUnit("minute");
            Hour = new TimeUnit("hour");
            Day = new TimeUnit("day");
            Week = new TimeUnit("week");
            Month = new TimeUnit("month");
            Byte = new ImmediateUnit("byte");
            KiloByte = new ImmediateUnit("byte", Scales.Kilo);
            MegaByte = new ImmediateUnit("byte", Scales.Mega);
            GigaByte = new ImmediateUnit("byte", Scales.Giga);
            TeraByte = new ImmediateUnit("byte", Scales.Tera);
            PetaByte = new ImmediateUnit("byte", Scales.Peta);
            Bit = new ImmediateUnit("bit");
            Degree = new ImmediateUnit("degree");
            Radian = new ImmediateUnit("radian");
            Celcius = new ImmediateUnit("celcius");
            Fahrenheit = new ImmediateUnit("fahrenheit");
            Kelvin = new ImmediateUnit("kelvin");
        }

    }
}
