package com.firerox.oplungiphone.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.SettingApplication;
import com.firerox.oplungiphone.SettingConfig;
import com.firerox.oplungiphone.database.DBHelper;
import com.firerox.oplungiphone.utility.DatabaseHandler;
import com.firerox.oplungiphone.utility.ImageLoader;
import com.firerox.oplungiphone.utility.JsonUtils;
import com.firerox.oplungiphone.utility.Product;
import com.firerox.oplungiphone.utility.ResourcesHelper;
import com.firerox.oplungiphone.view.ObservableStickyScrollView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    public DatabaseHandler db;
    static DBHelper dbHelper;
    public ImageLoader imageLoader;
    ViewPager viewpager;
    int position, pos;
    String[] str_products_id, str_category_id, str_category_image, str_category_name, str_title,
            str_image, str_detail, str_price, str_state, str_code;
    String products_id, products_category_id, products_category_name, products_title,
            products_image, products_detail, products_price, products_state, products_code;
    private Menu menu;
    //long product_id;
    //double product_price;
    int product_quantity;
    String product_name;
    TextView product_title_top, product_price, product_id, product_code, product_state;
    ProgressDialog progressDialog;
    JsonUtils utils;
    ObservableStickyScrollView scrollView;
    LinearLayout layout_content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        scrollView = (ObservableStickyScrollView) findViewById(R.id.container_content);
        layout_content = (LinearLayout) findViewById(R.id.layout_content);

        setupActionBar();

        progressDialog = new ProgressDialog(ProductDetailActivity.this);
        progressDialog.setMessage("Đang tải dữ liệu..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //renderViewBanner();

        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        db = new DatabaseHandler(this);
        dbHelper = new DBHelper(this);

        utils = new JsonUtils(getApplicationContext());

        Intent intent = getIntent();

        position = intent.getIntExtra("POSITION", 0);
        str_category_id = intent.getStringArrayExtra("CATEGORY_ITEM_CATEGORY_ID");
        str_category_name = intent.getStringArrayExtra("CATEGORY_ITEM_NAME");

        str_products_id = intent.getStringArrayExtra("CATEGORY_ITEM_PRODUCT_ID");
        str_image = intent.getStringArrayExtra("CATEGORY_ITEM_PRODUCT_IMAGE");
        str_title = intent.getStringArrayExtra("CATEGORY_ITEM_PRODUCT_TITLE");
        str_detail = intent.getStringArrayExtra("CATEGORY_ITEM_PRODUCT_DETAIL");
        str_price = intent.getStringArrayExtra("CATEGORY_ITEM_PRODUCT_PRICE");
        str_state = intent.getStringArrayExtra("CATEGORY_ITEM_PRODUCT_STATE");
        str_code = intent.getStringArrayExtra("CATEGORY_ITEM_PRODUCT_CODE");

        viewpager = (ViewPager) findViewById(R.id.news_slider);
        imageLoader = new ImageLoader(getApplicationContext());

        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewpager.setAdapter(adapter);

        boolean found = false;
        int j1 = 0;
        for (int i1 = 0; i1 < str_products_id.length; i1++) {
            if (str_products_id[i1].contains(String.valueOf(position))) {
                found = true;
                j1 = i1;
                break;
            }
        }
        if (found) {
            viewpager.setCurrentItem(j1);
        }

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                position = viewpager.getCurrentItem();
                products_id = str_products_id[position];

                List<Product> productList = db.getFavRow(products_id);
                /*if (productList.size() == 0) {
                    menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_shopping_cart_white_36x36));
                } else {
                    if (productList.get(0).getCategoryId().equals(products_id)) {
                        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_shopping_cart_white_36x36));
                    }
                }*/
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int position) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });

        // init analytics tracker
        ((SettingApplication) getApplication()).getTracker();

    }


    //Admob Banner
    private void renderViewBanner() {
        final AdView adView = (AdView) findViewById(R.id.adView);
        final ViewGroup bannerViewGroup = (ViewGroup) findViewById(R.id.container_admob);

        if (SettingConfig.ADMOB_RECIPE_BANNER && JsonUtils.isNetworkAvailable(ProductDetailActivity.this)) {

            AdRequest adRequest = new AdRequest.Builder().build();
            bannerViewGroup.setVisibility(View.VISIBLE);
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
            bannerViewGroup.setVisibility(View.GONE);
        }
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Bạn làm tốt lắm!");
        builder.setMessage("Sản phẩm này đã được thêm vào giỏ hàng.");

        builder.setPositiveButton("Tiếp tục mua",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        builder.setNegativeButton("Thanh toán",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void inputDialog(final int id, final String title, final int price){
        try{
            dbHelper.openDatabase();
        }catch(SQLException sql){
            throw sql;
        }

        final Dialog dialog = new Dialog(ProductDetailActivity.this, R.style.MyDialogTheme);
        dialog.setContentView(R.layout.custom_dialog_addcart);
        dialog.show();
        dialog.setCancelable(false);

        final EditText etQuantity = (EditText) dialog.findViewById(R.id.et_dialog_quantity);
        final Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        int maxlenght = 2;
        etQuantity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlenght)});
        etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = etQuantity.getText().toString();
                int quantity = 0;
                if (!temp.equalsIgnoreCase("")) {
                    quantity = Integer.parseInt(temp);
                    if (dbHelper.isDataExist(id)) {
                        dbHelper.updateData(id, quantity, (price * quantity));
                    } else {
                        dbHelper.addData(id,
                                title, quantity, (price * quantity));
                    }

                    dialog.cancel();
                    showSuccessDialog();

                } else {
                    etQuantity.requestFocus();
                    etQuantity.setError("Bạn chưa nhập số lượng");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        //Favorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                position = viewpager.getCurrentItem();
                products_title = str_title[position];
                products_detail = str_detail[position];
                String formattedString = android.text.Html.fromHtml(products_detail).toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        products_title + "\n" + formattedString + "\n" + " Chia sẻ sản phẩm từ ứng dụng Phương Bắc Phụ Kiện North.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.cart:
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void AddFavorite(int position) {
        products_category_id = str_category_id[position];
        products_id = str_products_id[position];
        products_category_name = str_category_name[position];
        products_title = str_title[position];
        products_image = str_image[position];
        products_detail = str_detail[position];
        products_price = str_price[position];
        products_state = str_state[position];
        products_code = str_code[position];

        db.AddtoFavorite(new Product(products_category_id, products_id, products_category_name,
                products_title, products_image, products_detail, products_price, products_state,
                products_code));
        Toast.makeText(getApplicationContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white));
    }*/

    //remove from favorite
    public void RemoveFavorite(int position) {
        products_category_id = str_category_id[position];
        db.RemoveFav(new Product(products_category_id));
        Toast.makeText(getApplicationContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_shopping_cart_white_36x36));
    }

    /*public void Favorite() {
        int first = viewpager.getCurrentItem();
        String image_id = str_products_id[first];

        List<Product> list = db.getFavRow(image_id);
        if (list.size() == 0) {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_shopping_cart_white_36x36));

        } else {
            if (list.get(0).getCategoryId().equals(image_id)) {
                menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.add_shopping_cart_white_36x36));
            }

        }
    }*/

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set toolbar background
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setVisibility(View.VISIBLE);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(null);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            // TODO Auto-generated constructor stub

            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return str_products_id.length;

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View imageLayout = inflater.inflate(R.layout.activity_detail_item, container, false);
            assert imageLayout != null;

            final ObservableStickyScrollView observableStickyScrollView = (ObservableStickyScrollView) imageLayout.findViewById(R.id.container_content);
            final View panelTopView = imageLayout.findViewById(R.id.toolbar_image_panel_top);
            final View panelBottomView = imageLayout.findViewById(R.id.toolbar_image_panel_bottom);
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.toolbar_image_imageview);
            product_title_top = (TextView) imageLayout.findViewById(R.id.toolbar_image_title);

            imageLoader.DisplayImage(SettingConfig.SERVER + "/uploads/products/" + str_image[position], imageView);

            product_title_top.setText(str_title[position]);

            product_id = (TextView) imageLayout.findViewById(R.id.product_id);
            product_id.setText(str_products_id[position]);

            product_code = (TextView) imageLayout.findViewById(R.id.activity_detail_product_code);
            product_code.setText(str_code[position]);

            product_price = (TextView) imageLayout.findViewById(R.id.activity_detail_product_price);
            product_price.setText(str_price[position]);

            product_state = (TextView) imageLayout.findViewById(R.id.activity_detail_product_state);
            if(str_state[position].equals("1")) {
                product_state.setText("Hết hàng");
            } else {
                product_state.setText("Còn hàng");
            }

            //TextView product_detail = (TextView) imageLayout.findViewById(R.id.tv_product_detail);
            //product_detail.setText(Html.fromHtml(str_detail[position]));
            //product_detail.setText(Html.fromHtml(str_title[position]));

            Button btnAddToCart = (Button) imageLayout.findViewById(R.id.btnAddToCart);
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputDialog(Integer.parseInt(str_products_id[position]), str_title[position],
                            Integer.parseInt(str_price[position]));
                }
            });

            str_detail[position] = str_detail[position].replace("/source/", "http://oplungiphone.net/source/");
            str_detail[position] = str_detail[position].replace("<img src", "<img style='display:block; max-width:100%; height:auto;' src");
            str_detail[position] = str_detail[position].replace("<video ", "<video style='display:block; max-width:100%; height:auto;' ");
            str_detail[position] = str_detail[position].replace("href=", "");

            WebView webViewDetail = (WebView) imageLayout.findViewById(R.id.webViewDetail);
            WebSettings webSettings = webViewDetail.getSettings();
            webViewDetail.loadDataWithBaseURL(null, str_detail[position], "text/html", "utf-8", null);
            webSettings.setTextSize(WebSettings.TextSize.SMALLER);
            webSettings.setDefaultFontSize(14);

            // scroll view
            observableStickyScrollView.setOnScrollViewListener(new ObservableStickyScrollView.ScrollViewListener() {
                private final int THRESHOLD = ProductDetailActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_gap_height);
                private final int PADDING_LEFT = ProductDetailActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_title_padding_right);
                private final int PADDING_BOTTOM = ProductDetailActivity.this.getResources().getDimensionPixelSize(R.dimen.global_spacing_xs);
                private final float SHADOW_RADIUS = 16;

                private int mPreviousY = 0;
                private ColorDrawable mTopColorDrawable = new ColorDrawable();
                private ColorDrawable mBottomColorDrawable = new ColorDrawable();


                @Override
                public void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy) {
                    // do not calculate if header is hidden
                    if (y > THRESHOLD && mPreviousY > THRESHOLD) return;

                    // calculate panel alpha
                    int alpha = (int) (y * (255f / (float) THRESHOLD));
                    if (alpha > 255) alpha = 255;

                    // set color drawables
                    mTopColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ProductDetailActivity.this, R.attr.colorPrimary));
                    mTopColorDrawable.setAlpha(alpha);
                    mBottomColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ProductDetailActivity.this, R.attr.colorPrimary));
                    mBottomColorDrawable.setAlpha(alpha);

                    // set panel background
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        panelTopView.setBackground(mTopColorDrawable);
                        panelBottomView.setBackground(mBottomColorDrawable);
                    } else {
                        panelTopView.setBackgroundDrawable(mTopColorDrawable);
                        panelBottomView.setBackgroundDrawable(mBottomColorDrawable);
                    }

                    // calculate image translation
                    float translation = y / 2;

                    // set image translation
                    imageView.setTranslationY(translation);

                    // calculate title padding
                    int paddingLeft = (int) (y * (float) PADDING_LEFT / (float) THRESHOLD);
                    if (paddingLeft > PADDING_LEFT) paddingLeft = PADDING_LEFT;

                    int paddingRight = PADDING_LEFT - paddingLeft;

                    int paddingBottom = (int) ((THRESHOLD - y) * (float) PADDING_BOTTOM / (float) THRESHOLD);
                    if (paddingBottom < 0) paddingBottom = 0;

                    // set title padding
                    product_title_top.setPadding(paddingLeft, 0, paddingRight, paddingBottom);

                    // calculate title shadow
                    float radius = ((THRESHOLD - y) * SHADOW_RADIUS / (float) THRESHOLD);

                    // set title shadow
                    product_title_top.setShadowLayer(radius, 0f, 0f, getResources().getColor(android.R.color.black));

                    // previous y
                    mPreviousY = y;
                }
            });

            // invoke scroll event because of orientation change toolbar refresh
            observableStickyScrollView.post(new Runnable() {
                @Override
                public void run() {
                    observableStickyScrollView.scrollTo(0, observableStickyScrollView.getScrollY() - 1);
                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

    }

}
