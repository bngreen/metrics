﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public interface IQuantity<T>
    {
        IUnit Unit { get; }
        T Value { get;}
    }
}
