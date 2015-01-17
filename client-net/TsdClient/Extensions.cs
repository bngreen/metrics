using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TsdClient
{
    public static class Extensions
    {
        public static IDictionary<TKey, TValue> ToDictionary<TSource, TKey, TValue>(
            this IEnumerable<TSource> source,
            Func<TSource, TKey> keySelector, 
            Func<TSource, TValue> valueSelector)
        {
            var dict = new Dictionary<TKey, TValue>();
            foreach (var sourceEntry in source)
            {
                dict.Add(
                    keySelector(sourceEntry),
                    valueSelector(sourceEntry)
                    );
            }
            return dict;
        }

        public static IDictionary<K, Vo> Map<K, Vi, Vo>(this IDictionary<K, Vi> dict, Func<Vi, Vo> mapper)
        {
            return (from x in dict select new { K = x.Key, V = mapper(x.Value) }).ToDictionary(d => d.K, d => d.V);
        }

    }
}
