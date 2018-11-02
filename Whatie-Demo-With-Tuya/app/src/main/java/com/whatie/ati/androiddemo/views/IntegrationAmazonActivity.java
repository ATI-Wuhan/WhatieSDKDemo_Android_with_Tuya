package com.whatie.ati.androiddemo.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 神火 on 2018/6/27.
 */

public class IntegrationAmazonActivity extends BaseActivity {
    private static final String TAG = "IntegrationAmazonActivi";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv_integration_amazon)
    WebView wvIntegrationAmazon;
    @BindView(R.id.pb_integration_detail)
    ProgressBar pbIntegrationDetail;

    @Override
    protected int getContentViewId() {
        return R.layout.act_integration_amazon;
    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.Use_Alexa));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {
        wvIntegrationAmazon.getSettings().setJavaScriptEnabled(true);

        wvIntegrationAmazon.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                StringBuilder errorString = new StringBuilder();
                switch (error.getPrimaryError()){
                    case SslError.SSL_NOTYETVALID:
                        errorString.append(getString(R.string.ssl_not_yet_valid));
                        break;
                    case SslError.SSL_EXPIRED:
                        errorString.append(getString(R.string.ssl_expired));
                        break;
                    case SslError.SSL_DATE_INVALID:
                        errorString.append(getString(R.string.ssl_date_invalid));
                        break;
                    case SslError.SSL_IDMISMATCH:
                        errorString.append(getString(R.string.ssl_is_mismatch));
                        break;
                    case SslError.SSL_INVALID:
                        errorString.append(getString(R.string.ssl_invalid));
                        break;
                    case SslError.SSL_UNTRUSTED:
                        errorString.append(getString(R.string.ssl_untrusted));
                        break;
                    default:
                        errorString.append(getString(R.string.ssl_error));
                        break;
                }
                errorString.append(getString(R.string.ssl_handler_process));


                new AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.ssl_warning_title))
                        .setMessage(errorString.toString())
                        .setPositiveButton(getString(R.string.ssl_error_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.proceed();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.ssl_error_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.cancel();
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
//                        try{
//                            HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory(getAssets().open("amazon.cer"));
//
//                            X509TrustManager trustManager = sslParams1.trustManager;
//
//                            Bundle bundle = SslCertificate.saveState(error.getCertificate());
//                            X509Certificate x509Certificate;
//                            byte[] bytes = bundle.getByteArray("x509-certificate");
//                            if (bytes == null) {
//                                x509Certificate = null;
//                            } else {
//                                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
//                                Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(bytes));
//                                x509Certificate = (X509Certificate) cert;
//                            }
//                            X509Certificate[] x509Certificates = new X509Certificate[1];
//                            x509Certificates[0] = x509Certificate;
//
//                            trustManager.checkServerTrusted(x509Certificates, "ECDH_RSA");
//                            handler.proceed();
//                        }catch (Exception e){
//                            handler.cancel();
//                            try {
//                                new AlertDialog.Builder(mContext)
//                                        .setTitle(getString(R.string.ssl_warning_title))
//                                        .setMessage(getString(R.string.ssl_error))
//                                        .setPositiveButton(getString(R.string.ssl_error_confirm), new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                                finish();
//                                            }
//                                        }).show();
//                            } catch (Exception x) {
//                                x.printStackTrace();
//                            }
//                        }
            }
        });
        wvIntegrationAmazon.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(pbIntegrationDetail != null){
                    if (newProgress == 100) {
                        pbIntegrationDetail.setVisibility(View.GONE);
                    } else {
                        if (View.INVISIBLE == pbIntegrationDetail.getVisibility()) {
                            pbIntegrationDetail.setVisibility(View.VISIBLE);
                        }
                        pbIntegrationDetail.setProgress(newProgress);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        wvIntegrationAmazon.loadUrl("https://users.whatie.net/jsp/index/amazonAccess.html");
    }

    @OnClick({R.id.ll_title_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wvIntegrationAmazon!=null){
            wvIntegrationAmazon.destroy();
        }
    }

}
