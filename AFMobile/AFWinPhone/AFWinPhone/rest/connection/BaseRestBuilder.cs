using AFWinPhone.rest.holder;

namespace AFWinPhone.rest.connection
{
    public abstract class BaseRestBuilder : Reselization
    {
        public abstract object reselialize(AFDataHolder componentData);
    }
}
