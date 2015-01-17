using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using log4net;

namespace TsdClient
{
    public class TsdQueryLogSink : ISink
    {
        public class Builder
        {
            public string Path { get; private set; }
            public string Name { get; private set; }
            public string Extension { get; private set; }
            public bool ImmediateFlush { get; private set; }
            public JSONLogFormat.Versions JSONLogVersion { get; set; }

            private string DatePattern { get; set; }

            public int MaxRollBackups { get; private set; }

            public Builder(string name)
            {
                Extension = "log";
                Path = "";
                Name = name;
                ImmediateFlush = true;
                JSONLogVersion = JSONLogFormat.JsonLogSerializerFactory.LatestVersion;
                DatePattern = ".yyyy-MM-dd-HH";
                MaxRollBackups = 24;
            }

            public Builder SetName(string value)
            {
                Name = value;
                return this;
            }

            public Builder SetPath(string value)
            {
                Path = value;
                return this;
            }
            public Builder SetImmediateFlush(bool value)
            {
                ImmediateFlush = value;
                return this;
            }

            public Builder SetExtension(string value)
            {
                Extension = value;
                return this;
            }

            public Builder RollDaily()
            {
                DatePattern = ".yyyy-MM-dd";
                return this;
            }

            public Builder RollEveryMinute()
            {
                DatePattern = ".yyyy-MM-dd-mm";
                return this;
            }

            public Builder SetMaxRollBackups(int value)
            {
                MaxRollBackups = value;
                return this;
            }

            public string Filename
            {
                get
                {
                    if (Extension == "")
                        return System.IO.Path.Combine(Path, Name);
                    return System.IO.Path.Combine(Path, String.Format("{0}.{1}", Name, Extension));
                }
            }

            public ISink Build()
            {
                return new TsdQueryLogSink(Filename, DatePattern, MaxRollBackups, ImmediateFlush, JSONLogVersion);
            }

        }

        private Common.Logging.ILog Logger { get; set; }

        private ILog SampleLogger { get; set; }
        private JSONLogFormat.IJsonLogSerializer Serializer { get; set; }

        internal TsdQueryLogSink(string filename, string datePattern, int maxRollBackups, bool immediateFlush, JSONLogFormat.Versions version)
        {
            Logger = Common.Logging.LogManager.GetLogger(typeof(TsdQueryLogSink));

            var repos = LogManager.CreateRepository("SampleLoggerRepos");
            var appender = new log4net.Appender.RollingFileAppender();
            //appender.MaxFileSize = maxFileSize;
            appender.RollingStyle = log4net.Appender.RollingFileAppender.RollingMode.Date;
            appender.MaxSizeRollBackups = maxRollBackups;
            appender.File = filename;
            appender.Layout = new log4net.Layout.PatternLayout("%message%newline");
            appender.ImmediateFlush = immediateFlush;
            appender.StaticLogFileName = false;
            appender.DatePattern = datePattern;
            appender.ActivateOptions();
            log4net.Config.BasicConfigurator.Configure(repos, appender);
            SampleLogger = LogManager.GetLogger("SampleLoggerRepos", "SampleLogger");

            Serializer = JSONLogFormat.JsonLogSerializerFactory.CreateSerializer(version);
        }

        public void Record(Sample sample)
        {
            try
            {
                SampleLogger.Info(Serializer.Serialize(sample));
            }
            catch (Exception e)
            {
                Logger.Warn(m => m("Exception serializing and writing metrics {0}", e));
            }
        }
    }
}
