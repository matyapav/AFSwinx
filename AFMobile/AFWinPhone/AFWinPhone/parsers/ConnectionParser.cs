using AFWinPhone.rest.connection;
using System;
using System.Collections.Generic;
using System.Text;
using Windows.Data.Xml.Dom;
using Windows.Web.Http;
using AFWinPhone.utils;

namespace AFWinPhone.parsers
{
    class ConnectionParser : XMLParser
    {

        // Tags which separate connections
        private const String CONNECTION_TAG = "connection";
        private const String CONNECTION_ID_ATTRIBUTE = "id";

        // Tags which separate connection type
        private const String METAMODEL_CONNECTION = "metaModel";
        private const String DATA_CONNECTION = "data";
        private const String SEND_CONNECTION = "send";
        private const String REMOVE_CONNECTION = "remove";

        // Tags which will specify security
        private const String SECURITY_METHOD = "security-method";
        private const String USER_NAME = "userName";
        private const String PASSOWORD = "password";


        // Concrete value of connection
        private const String END_POINT = "endPoint";
        private const String END_POINT_PARAMETERS = "endPointParameters";
        private const String PROTOCOL = "protocol";
        private const String PORT = "port";
        private const String HEADER_PARAM = "header-param";
        private const String SECURITY_PARAMS = "security-params";
        private const String PARAM = "param";
        private const String VALUE = "value";
        private const String CONTENT_TYPE = "content-type";
        private const String ACCEPT_TYPE = "accept-type";
        private const String HTTP_METHOD = "method";

        private Dictionary<String, String> elConnectionData;

        // Flag if EL evaluation will be done
        private bool doElEvaluation = false;
        // Id of connection to parse
        private String connectionId;

        public ConnectionParser(String id, Dictionary<String, String> elConnectionData)
        {
            this.connectionId = id;
            if (elConnectionData != null && elConnectionData.Count != 0)
            {
                this.elConnectionData = elConnectionData;
                doElEvaluation = true;
            }
        }


        public ConnectionParser(String id)
        {
            this.connectionId = id;
        }

        public AFSwinxConnectionPack parseDocument(XmlDocument documentToParse)
        {
            // Prepare connection pack
            AFSwinxConnectionPack connectionPack = new AFSwinxConnectionPack();
            // Find root of document
            XmlNodeList childs = documentToParse.GetElementsByTagName(CONNECTION_TAG);
            foreach (IXmlNode connectionRoot in childs)
            {
                if (connectionRoot.NodeType != NodeType.ElementNode)
                {
                    continue;
                }
                // For each connection check if it's id is id which must be parsed
                XmlElement nodeElement = (XmlElement)connectionRoot;
                if (nodeElement.GetAttribute(CONNECTION_ID_ATTRIBUTE).Equals(connectionId))
                {
                    XmlNodeList connectionsTypes = connectionRoot.ChildNodes;
                    for (uint j = 0; j < connectionsTypes.Length; j++)
                    {
                        IXmlNode concreteConnection = connectionsTypes.Item(j);
                        if (concreteConnection.NodeType != NodeType.ElementNode)
                        {
                            continue;
                        }
                        String connectionName = concreteConnection.NodeName;
                        XmlNodeList nodeProperties = concreteConnection.ChildNodes;
                        // Create new connection
                        AFSwinxConnection connection = new AFSwinxConnection();
                        for (uint k = 0; k < nodeProperties.Length; k++)
                        {
                            // Set connection properties
                            IXmlNode property = nodeProperties.Item(k);
                            String nodeName = property.NodeName;
                            String nodeValue = property.InnerText;
                            if (nodeName.Equals(END_POINT))
                            {
                                connection.setAddress(evaluateEL(nodeValue));
                            }
                            else if (nodeName.Equals(END_POINT_PARAMETERS))
                            {
                                connection.setParameters(evaluateEL(nodeValue));
                            }
                            else if (nodeName.Equals(PROTOCOL))
                            {
                                connection.setProtocol(evaluateEL(nodeValue));
                            }
                            else if (nodeName.Equals(PORT))
                            {
                                String port = evaluateEL(nodeValue);
                                if (!String.IsNullOrEmpty(port))
                                {
                                    connection.setPort(Convert.ToInt32(evaluateEL(nodeValue)));
                                }
                            }
                            else if (nodeName.Equals(HEADER_PARAM))
                            {
                                parseHeaderParam(connection, property.ChildNodes);
                            }
                            else if (nodeName.Equals(HTTP_METHOD))
                            {
                                String method = evaluateEL(nodeValue);
                                if (method.ToLower().Equals("get"))
                                {
                                    connection.setHttpMethod(HttpMethod.Get);
                                }
                                else if (method.ToLower().Equals("post"))
                                {
                                    connection.setHttpMethod(HttpMethod.Post);
                                }
                                else if (method.ToLower().Equals("put"))
                                {
                                    connection.setHttpMethod(HttpMethod.Put);
                                }
                                else if (method.ToLower().Equals("delete"))
                                {
                                    connection.setHttpMethod(HttpMethod.Delete);
                                }
                                else
                                {
                                    //method not supported
                                }
                                //get method
                            }
                            else if (nodeName.Equals(SECURITY_PARAMS))
                            {
                                parseSecurityParams(connection, property.ChildNodes);
                            }
                        }
                        // Set created connection to connection holder based on connection type
                        if (connectionName.Equals(METAMODEL_CONNECTION))
                        {
                            connection.setHttpMethod(HttpMethod.Get);
                            connectionPack.setMetamodelConnection(connection);
                        }
                        else if (connectionName.Equals(DATA_CONNECTION))
                        {
                            connection.setHttpMethod(HttpMethod.Get);
                            connectionPack.setDataConnection(connection);
                        }
                        else if (connectionName.Equals(SEND_CONNECTION))
                        {
                            if (connection.getHttpMethod() == null)
                            {
                                connection.setHttpMethod(HttpMethod.Post);
                            }
                            connectionPack.setSendConnection(connection);
                        }
                        else if (connectionName.Equals(REMOVE_CONNECTION))
                        {
                            if (connection.getHttpMethod() == null)
                            {
                                connection.setHttpMethod(HttpMethod.Delete);
                            }
                        }
                    }
                }
            }
            return connectionPack;
        }

        private void parseSecurityParams(AFSwinxConnection connection, XmlNodeList securityParams)
        {
            ConnectionSecurity security = new ConnectionSecurity();
            for (uint i = 0; i < securityParams.Length; i++)
            {
                IXmlNode node = securityParams.Item(i);
                String nodeName = node.NodeName;
                String nodeValue = evaluateEL(node.InnerText).ToLower();
                if (nodeName.Equals(SECURITY_METHOD))
                {
                    if (nodeValue.Equals(SecurityMethod.BASIC.toString()))
                    {
                        security.setMethod(SecurityMethod.BASIC);
                    }
                }
                else if (nodeName.Equals(USER_NAME))
                {
                    security.setUserName(nodeValue);
                }
                else if (nodeName.Equals(PASSOWORD))
                {
                    security.setPassword(nodeValue);
                }
            }
            connection.setSecurity(security);
        }

        private void parseHeaderParam(AFSwinxConnection connection, XmlNodeList headerParam)
        {
            String key = "";
            String value = "";
            for (uint i = 0; i < headerParam.Length; i++)
            {
                IXmlNode concreteParam = headerParam.Item(i);
                if (concreteParam.NodeType != NodeType.ElementNode)
                {
                    continue;
                }
                String nodeName = concreteParam.NodeName;
                String nodeValue = concreteParam.InnerText;
                if (nodeName.Equals(PARAM))
                {
                    key = evaluateEL(nodeValue);
                }
                else if (nodeName.Equals(VALUE))
                {
                    value = evaluateEL(nodeValue);
                }
            }
            // Parse content type and accept type separately
            if (key.Equals(CONTENT_TYPE))
            {
                /* connection.setContentType((HeaderType)Utils.getEnumFromString(
                         HeaderType.class, value, true));*/
            }
            else if (key.Equals(ACCEPT_TYPE))
            {
                /*connection.setAcceptedType((HeaderType) Utils.getEnumFromString(
                        HeaderType.class, value, true));*/
            }
            else {
                connection.addHeaderParam(key, value);
            }
        }

        private String evaluateEL(String nodeValue)
        {
            if (doElEvaluation && !String.IsNullOrEmpty(nodeValue))
            {
                return Utils.evaluateElExpression(nodeValue, elConnectionData);
            }
            return nodeValue;
        }

      

    }
}
