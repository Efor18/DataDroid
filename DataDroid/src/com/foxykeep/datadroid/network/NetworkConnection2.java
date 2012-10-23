/**
 * 2011 Foxykeep (http://datadroid.foxykeep.com)
 * <p>
 * Licensed under the Beerware License : <br />
 * As long as you retain this notice you can do whatever you want with this stuff. If we meet some
 * day, and you think this stuff is worth it, you can buy me a beer in return
 */

package com.foxykeep.datadroid.network;

import android.os.Build;
import android.util.Log;

import com.foxykeep.datadroid.config.LogConfig;
import com.foxykeep.datadroid.exception.CompulsoryParameterException;
import com.foxykeep.datadroid.internal.network.NetworkConnectionImplFY;
import com.foxykeep.datadroid.internal.network.NetworkConnectionImplGB;

import org.apache.http.Header;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class gives the user an API to easily call a webservice and return the received response.
 * <p>
 * Use the {@link NetworkConnection2Builder} to prepare your webservice call.
 * 
 * @author Foxykeep
 */
public final class NetworkConnection2 {

    private static final String LOG_TAG = NetworkConnection2.class.getSimpleName();

    private NetworkConnection2() {
        // No public constructor
    }

    public static enum Method {
        GET, POST, PUT, DELETE
    }

    /**
     * The result of a webservice call.
     * <p>
     * Contains the {@link Header}s of the response and the body of the response as an unparsed
     * <code>String</code>.
     * 
     * @author Foxykeep
     */
    public static class WebserviceResult {

        public Header[] headerArray;
        public String body;

        public WebserviceResult(final Header[] headerArray, final String body) {
            this.headerArray = headerArray;
            this.body = body;
        }
    }

    /**
     * Builder used to create a {@link NetworkConnection2}.
     * 
     * @author foxykeep
     */
    public static class NetworkConnection2Builder {
        private String mUrl;
        private Method mMethod = Method.GET;
        private HashMap<String, String> mParameterMap = null;
        private ArrayList<Header> mHeaderList = null;
        private boolean mIsGzipEnabled = true;
        private String mUserAgent = null;
        private String mPostText = null;
        private UsernamePasswordCredentials mCredentials = null;
        private boolean mIsSslValidationEnabled = true;

        public NetworkConnection2Builder(final String url) {
            if (url == null) {
                if (LogConfig.DD_ERROR_LOGS_ENABLED) {
                    Log.e(LOG_TAG,
                            "NetworkConnection2Builder - Compulsory Parameter : request URL has not been set.");
                }
                throw new CompulsoryParameterException("Request URL has not been set.");
            }
            mUrl = url;
        }

        /**
         * Set the method to use. Default is {@link Method#GET}.
         * 
         * @param method The method to use.
         * @return The builder.
         */
        public NetworkConnection2Builder setMethod(final Method method) {
            mMethod = method;
            return this;
        }

        /**
         * Set the parameters to add to the request. This has to be a "key" => "value" Map.
         * 
         * @param parameterMap The parameters to add to the request.
         * @return The builder.
         */
        public NetworkConnection2Builder setParameters(final HashMap<String, String> parameterMap) {
            mParameterMap = parameterMap;
            return this;
        }

        /**
         * Set the headers to add to the request.
         * 
         * @param headerList The headers to add to the request.
         * @return The builder.
         */
        public NetworkConnection2Builder setHeaderList(final ArrayList<Header> headerList) {
            mHeaderList = headerList;
            return this;
        }

        /**
         * Set whether the request will use gzip compression if available on the server. Default is
         * true.
         * 
         * @param isGzipEnabled Whether the request will user gzip compression if available on the
         *            server.
         * @return The builder.
         */
        public NetworkConnection2Builder setGzipEnabled(final boolean isGzipEnabled) {
            mIsGzipEnabled = isGzipEnabled;
            return this;
        }

        // STOPSHIP add the name of the method to create the default one used
        /**
         * Set the user agent to set in the request. Otherwise a default Android one will be used.
         * <p>
         * For more information about the default one used, check the method XXX
         * 
         * @param userAgent The user agent.
         * @return The builder.
         */
        public NetworkConnection2Builder setUserAgent(final String userAgent) {
            mUserAgent = userAgent;
            return this;
        }

        /**
         * Set the POSTDATA text that will be added in the request. Also automatically set the
         * {@link Method} to {@link Method#POST} to be able to use it.
         * 
         * @param postText The POSTDATA text that will be added in the request.
         * @return The builder.
         */
        public NetworkConnection2Builder setPostText(final String postText) {
            mPostText = postText;
            mMethod = Method.POST;
            return this;
        }

        // TODO check http://bit.ly/T7lZEm for implementation code.
        /**
         * Set the credentials to use for authentication.
         * 
         * @param credentials The credentials to use for authentication.
         * @return The builder.
         */
        public NetworkConnection2Builder setCredentials(UsernamePasswordCredentials credentials) {
            mCredentials = credentials;
            return this;
        }

        // TODO check http://bit.ly/XgZpYg for implementation code.
        /**
         * Set whether the request will validate the SSL certificates. Default is true.
         * 
         * @param enabled Whether the request will validate the SSL certificates.
         * @return The Builder.
         */
        public NetworkConnection2Builder setSslValidationEnabled(boolean enabled) {
            mIsSslValidationEnabled = enabled;
            return this;
        }

        // TODO add the exceptions
        /**
         * Execute the webservice call and return the {@link WebserviceResult}.
         * 
         * @return The result of the webservice call.
         */
        public WebserviceResult execute() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return NetworkConnectionImplFY.execute(mUrl, mMethod, mParameterMap, mHeaderList,
                        mIsGzipEnabled, mUserAgent, mPostText, mCredentials,
                        mIsSslValidationEnabled);
            } else {
                return NetworkConnectionImplGB.execute(mUrl, mMethod, mParameterMap, mHeaderList,
                        mIsGzipEnabled, mUserAgent, mPostText, mCredentials,
                        mIsSslValidationEnabled);
            }
        }
    }
}