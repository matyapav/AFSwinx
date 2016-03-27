using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AFWinPhone.enums;

namespace AFWinPhone.components.parts
{
    public class Layout : TopLevelLayout
    {

        private LabelPosition labelPosition;

        public Layout()
        {
            
        }

        public LabelPosition getLabelPosition()
        {
            return labelPosition;
        }

        public void setLabelPosition(LabelPosition position)
        {
            this.labelPosition = position;
        }
    }
}
