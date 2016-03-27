using System;
using System.Collections.Generic;

namespace AFWinPhone.components.parts
{
    public class AFClassInfo
    {
        private String className;
        private TopLevelLayout layout;
        private List<AFFieldInfo> fieldInfos;
        private List<AFClassInfo> innerClasses;

        public AFClassInfo(String className)
        {
            this.className = className;
        }

        public void addFieldInfo(AFFieldInfo afField)
        {
            if (fieldInfos == null)
            {
                fieldInfos = new List<AFFieldInfo>();
            }
            fieldInfos.Add(afField);
        }

        public void addInnerClass(AFClassInfo innerClass)
        {
            if (innerClasses == null)
            {
                innerClasses = new List<AFClassInfo>();
            }
            innerClasses.Add(innerClass);
        }

        //GETTERS AND SETTERS

        public void setClassName(String className)
        {
            this.className = className;
        }

        public String getClassName()
        {
            return this.className;
        }

        public void setLayout(TopLevelLayout properties)
        {
            this.layout = properties;
        }

        public TopLevelLayout getLayout()
        {
            return this.layout;
        }

        public List<AFFieldInfo> getFieldInfos()
        {
            return this.fieldInfos;
        }

        public void setFieldInfos(List<AFFieldInfo> fieldInfos)
        {
            this.fieldInfos = fieldInfos;
        }

        public List<AFClassInfo> getInnerClasses()
        {
            return this.innerClasses;
        }



    }
}
