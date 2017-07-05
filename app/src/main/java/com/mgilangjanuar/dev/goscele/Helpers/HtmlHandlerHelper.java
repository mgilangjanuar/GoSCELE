package com.mgilangjanuar.dev.goscele.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.InAppBrowserActivity;

import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

/**
 * Created by muhammadgilangjanuar on 5/20/17.
 * source: https://gist.github.com/mlakkadshaw/5983704 and
 * http://stackoverflow.com/questions/12418279/android-textview-with-clickable-links-how-to-capture-clicks
 * http://stackoverflow.com/a/22937381
 * with modification
 */

public class HtmlHandlerHelper implements Html.TagHandler {

    public String html;
    public boolean isSetSpan = true;

    private Context context;
    private int listItemCount = 0;
    private Vector<String> listParents = new Vector<>();

    public HtmlHandlerHelper(Context context, String html) {
        this.html = html;
        this.context = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equals("ul") || tag.equals("ol") || tag.equals("dd")) {
            if (opening) {
                listParents.add(tag);
            } else listParents.remove(tag);

            listItemCount = 0;
        } else if (tag.equals("li") && !opening) {
            handleListTag(output);
        } else if (tag.equalsIgnoreCase("code")) {
            if (opening) {
                output.setSpan(new TypefaceSpan("monospace"), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
            } else {
                Log.d("COde Tag", "Code tag encountered");
                Object obj = getLast(output, TypefaceSpan.class);
                int where = output.getSpanStart(obj);

                output.setSpan(new TypefaceSpan("monospace"), where, output.length(), 0);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        } else {
            for (int i = objs.length; i > 0; i--) {
                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i - 1];
                }
            }
            return null;
        }
    }

    private void handleListTag(Editable output) {
        if (listParents.lastElement().equals("ul")) {
            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            if (isSetSpan) {
                output.setSpan(new BulletSpan(15 * listParents.size()), start, output.length(), 0);
            }
        } else if (listParents.lastElement().equals("ol")) {
            listItemCount++;
            output.append("\n");
            String[] split = output.toString().split("\n");

            int lastIndex = split.length - 1;
            int start = output.length() - split[lastIndex].length() - 1;
            output.insert(start, listItemCount + ". ");
            if (isSetSpan) {
                output.setSpan(new LeadingMarginSpan.Standard(15 * listParents.size()), start, output.length(), 0);
            }
        }
    }

    private CharSequence trimTrailingWhitespace(CharSequence source) {

        if (source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        int j = 0;
        while (++j < source.length() && Character.isWhitespace(source.charAt(j))) {
        }

        return source.subSequence(j - 1, i + 1);
    }


    private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                context.startActivity((new Intent(context, InAppBrowserActivity.class)).putExtra("url", span.getURL()));
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public void setTextViewHTML(final TextView text) {
        if (html == null) {
            return;
        }
        CharSequence sequence;
        try {
            sequence = trimTrailingWhitespace(Html.fromHtml(html, new URLImageParser(text, context), this));
        } catch (Exception e) {
            this.isSetSpan = false;
            sequence = trimTrailingWhitespace(Html.fromHtml(html, new URLImageParser(text, context), this));
        }
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public class URLImageParser implements Html.ImageGetter {
        Context context;
        View container;

        public URLImageParser(View container, Context context) {
            this.context = context;
            this.container = container;
        }

        public Drawable getDrawable(String source) {
            if (source.matches("data:image.*base64.*")) {
                String base_64_source = source.replaceAll("data:image.*base64", "");
                byte[] data = Base64.decode(base_64_source, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Drawable image = new BitmapDrawable(context.getResources(), bitmap);
                image.setBounds(0, 0, 0 + image.getIntrinsicWidth(), 0 + image.getIntrinsicHeight());
                return image;
            } else {
                URLDrawable urlDrawable = new URLDrawable();
                ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
                asyncTask.execute(source);
                return urlDrawable; //return reference to URLDrawable where We will change with actual image from the src tag
            }
        }

        public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
            URLDrawable urlDrawable;

            public ImageGetterAsyncTask(URLDrawable d) {
                this.urlDrawable = d;
            }

            @Override
            protected Drawable doInBackground(String... params) {
                String source = params[0];
                return fetchDrawable(source);
            }

            @Override
            protected void onPostExecute(Drawable result) {
                if (result == null) {
                    return;
                }
                urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight()); //set the correct bound according to the result from HTTP call
                urlDrawable.drawable = result; //change the reference of the current drawable to the result from the HTTP call
                URLImageParser.this.container.invalidate(); //redraw the image by invalidating the container
            }

            public Drawable fetchDrawable(String urlString) {
                try {
                    InputStream is = (InputStream) new URL(urlString).getContent();
                    Drawable drawable = Drawable.createFromStream(is, "src");
                    drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
                    return drawable;
                } catch (Exception e) {
                    return null;
                }
            }
        }
    }

    public class URLDrawable extends BitmapDrawable {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}
