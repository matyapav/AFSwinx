using AFWinPhone.rest.connection;
using System;
using System.Diagnostics;
using System.Threading;
using System.Threading.Tasks;
using Windows.Data.Json;
using Windows.Security.Cryptography;
using Windows.Storage.Streams;
using Windows.Web.Http;
using Windows.Web.Http.Headers;

namespace AFWinPhone.rest
{
    class RequestMaker
    {
        private HeaderType headerType;
        private HttpMethod httpMethod;
        private ConnectionSecurity security;
        private String address;
        private Object data;

        public RequestMaker(HttpMethod method, HeaderType headerType,
                           ConnectionSecurity security, Object data, String url)
        {
            this.headerType = headerType;
            this.httpMethod = method;
            this.security = security;
            this.address = url;
            this.data = data;
        }

        public async Task<String> doRequest()
        {

            HttpClient httpClient = new HttpClient();
            var cancellationTokenSource = new CancellationTokenSource(5000);
            httpClient.DefaultRequestHeaders.Accept.Add(new HttpMediaTypeWithQualityHeaderValue(headerType.ToString()));

            //ContentRoot.Children.Add(ring); //TODO add ring
            if (security != null)
            {
                if (security.getMethod().Equals(SecurityMethod.BASIC))
                {
                    IBuffer buffer = CryptographicBuffer.ConvertStringToBinary((security.getUserName() + ":" + security.getPassword()), BinaryStringEncoding.Utf8);
                    String encoded = CryptographicBuffer.EncodeToBase64String(buffer);
                    httpClient.DefaultRequestHeaders.Authorization = new HttpCredentialsHeaderValue("Basic", encoded);
                    Debug.WriteLine("Authorization object "+ httpClient.DefaultRequestHeaders.Authorization);
                    Debug.WriteLine("SECURITY " + "Basic " + encoded + " for "+security.getUserName()+":"+security.getPassword());
                }
            }

            String dataStr = "";
            if (data != null && (httpMethod.Equals(HttpMethod.Post) || httpMethod.Equals(HttpMethod.Put)))
            {
                Debug.WriteLine("DATA " + ((JsonObject)data).Stringify());
                dataStr = ((JsonObject)data).Stringify();
            }
          
            HttpStringContent content = new HttpStringContent(dataStr, Windows.Storage.Streams.UnicodeEncoding.Utf8, headerType.ToString());
            HttpRequestMessage htm = new HttpRequestMessage(httpMethod, new Uri(address));
            htm.Content = content;
            HttpResponseMessage response = await httpClient.SendRequestAsync(htm).AsTask(cancellationTokenSource.Token).ConfigureAwait(false);

            int responseCode = (int) response.StatusCode;
            String responseMsg = response.ReasonPhrase;
            Debug.WriteLine("RESPONSE CODE " + responseCode);
            if (responseCode < 200 || responseCode >= 300)
            {
                throw new Exception(responseCode + " " + responseMsg);
            }

            return await response.Content.ReadAsStringAsync();         
        }

    }
}
