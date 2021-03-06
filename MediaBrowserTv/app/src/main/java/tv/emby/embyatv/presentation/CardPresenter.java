package tv.emby.embyatv.presentation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import mediabrowser.model.dto.BaseItemDto;
import mediabrowser.model.livetv.ChannelInfoDto;
import mediabrowser.model.livetv.ProgramInfoDto;
import mediabrowser.model.livetv.RecordingInfoDto;
import tv.emby.embyatv.R;
import tv.emby.embyatv.itemhandling.BaseRowItem;
import tv.emby.embyatv.util.Utils;

public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static Context mContext;

    public CardPresenter() {
        super();
    }

    static class ViewHolder extends Presenter.ViewHolder {
        private int cardWidth = 230;

        private int cardHeight = 280;
        private BaseRowItem mItem;
        private MyImageCardView mCardView;
        private Drawable mDefaultCardImage;
        private PicassoImageCardViewTarget mImageCardViewTarget;

        public ViewHolder(View view) {
            super(view);
            mCardView = (MyImageCardView) view;

            mImageCardViewTarget = new PicassoImageCardViewTarget(mCardView);
            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.video);
        }

        public int getCardHeight() {
            return cardHeight;
        }

        public void setItem(BaseRowItem m) {
            mItem = m;
            switch (mItem.getItemType()) {

                case BaseItem:
                    BaseItemDto itemDto = mItem.getBaseItem();
                    Double aspect = Utils.getImageAspectRatio(itemDto, m.getPreferParentThumb());
                    switch (itemDto.getType()) {
                        case "Audio":
                        case "MusicAlbum":
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.audio);
                            break;
                        case "Person":
                        case "MusicArtist":
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.person);
                            break;
                        case "Season":
                        case "Series":
                        case "Episode":
                            //TvApp.getApplication().getLogger().Debug("**** Image width: "+ cardWidth + " Aspect: " + Utils.getImageAspectRatio(itemDto, m.getPreferParentThumb()) + " Item: "+itemDto.getName());
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.tv);
                            break;
                        case "CollectionFolder":
                            if (aspect == null) aspect = 1.779;
                        case "Folder":
                        case "MovieGenreFolder":
                        case "MusicGenreFolder":
                        case "MovieGenre":
                        case "Genre":
                        case "MusicGenre":
                        case "UserView":
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.folder);
                            break;
                        default:
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.video);
                            break;

                    }
                    if (aspect == null) aspect = .77777;
                    cardHeight = !m.isStaticHeight() ? aspect > 1 ? 280 : 350 : 315;
                    cardWidth = (int)((aspect) * cardHeight);
                    if (cardWidth < 10) cardWidth = 230;  //Guard against zero size images causing picasso to barf
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    break;
                case LiveTvChannel:
                    ChannelInfoDto channel = mItem.getChannelInfo();
                    Double tvAspect = channel.getPrimaryImageAspectRatio();
                    if (tvAspect == null) tvAspect = .7777777;
                    cardHeight = !m.isStaticHeight() ? tvAspect > 1 ? 280 : 350 : 315;
                    cardWidth = (int)((tvAspect) * cardHeight);
                    if (cardWidth < 10) cardWidth = 230;  //Guard against zero size images causing picasso to barf
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.tv);
                    break;

                case LiveTvProgram:
                    ProgramInfoDto program = mItem.getProgramInfo();
                    Double programAspect = program.getPrimaryImageAspectRatio();
                    if (programAspect == null) programAspect = .7777777;
                    cardHeight = programAspect > 1 ? 280 : 350;
                    cardWidth = (int)((programAspect) * cardHeight);
                    if (cardWidth < 10) cardWidth = 230;  //Guard against zero size images causing picasso to barf
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.tv);
                    break;

                case LiveTvRecording:
                    RecordingInfoDto recording = mItem.getRecordingInfo();
                    Double recordingAspect = recording.getPrimaryImageAspectRatio();
                    if (recordingAspect == null) recordingAspect = .7777777;
                    cardHeight = !m.isStaticHeight() ? recordingAspect > 1 ? 280 : 350 : 315;
                    cardWidth = (int)((recordingAspect) * cardHeight);
                    if (cardWidth < 10) cardWidth = 230;  //Guard against zero size images causing picasso to barf
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.tv);
                    break;

                case Server:
                    cardWidth = (int)(.777777777 * cardHeight);
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.server);
                case Person:
                    cardWidth = (int)(.777777777 * cardHeight);
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.person);
                    break;
                case User:
                    cardWidth = (int)(.777777777 * cardHeight);
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.person);
                    break;
                case Chapter:
                    cardWidth = (int)(1.779 * cardHeight);
                    mCardView.setMainImageDimensions(cardWidth, cardHeight);
                    mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.video);
                    break;
                case SearchHint:
                    switch (mItem.getSearchHint().getType()) {
                        case "Episode":
                            cardWidth = (int)(1.779 * cardHeight);
                            mCardView.setMainImageDimensions(cardWidth, cardHeight);
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.tv);
                            break;
                        case "Person":
                            cardWidth = (int)(.777777777 * cardHeight);
                            mCardView.setMainImageDimensions(cardWidth, cardHeight);
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.person);
                            break;
                        default:
                            cardWidth = (int)(.777777777 * cardHeight);
                            mCardView.setMainImageDimensions(cardWidth, cardHeight);
                            mDefaultCardImage = mContext.getResources().getDrawable(R.drawable.video);
                            break;
                    }
            }
        }

        public BaseRowItem getItem() {
            return mItem;
        }

        public ImageCardView getCardView() {
            return mCardView;
        }

        protected void updateCardViewImage(String url) {
            if (url == null) {
                Picasso.with(mContext)
                        .load("nothing")
                        .resize(cardWidth, cardHeight)
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(mImageCardViewTarget);

            } else {
                Picasso.with(mContext)
                        .load(url)
                        .skipMemoryCache()
                        .resize(cardWidth, cardHeight)
                        .centerCrop()
                        .error(mDefaultCardImage)
                        .into(mImageCardViewTarget);
            }
        }

        protected void resetCardViewImage() {
            Picasso.with(mContext)
                    .load(Uri.parse("android.resource://tv.emby.embyatv/drawable/loading"))
                    .skipMemoryCache()
                    .resize(cardWidth, cardHeight)
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(mImageCardViewTarget);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        //Log.d(TAG, "onCreateViewHolder");
        mContext = parent.getContext();

        MyImageCardView cardView = new MyImageCardView(mContext);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.lb_basic_card_info_bg_color));
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (!(item instanceof BaseRowItem)) return;
        BaseRowItem rowItem = (BaseRowItem) item;

        ((ViewHolder) viewHolder).setItem(rowItem);

        //Log.d(TAG, "onBindViewHolder");
        ((ViewHolder) viewHolder).mCardView.setTitleText(rowItem.getFullName());
        ((ViewHolder) viewHolder).mCardView.setContentText(rowItem.getSubText());
        Drawable badge = rowItem.getBadgeImage();
        if (badge != null) {
            ((ViewHolder) viewHolder).mCardView.setBadgeImage(badge);

        }

        ((ViewHolder) viewHolder).updateCardViewImage(rowItem.getPrimaryImageUrl(((ViewHolder) viewHolder).getCardHeight()));

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        //Log.d(TAG, "onUnbindViewHolder");
        //Get the image out of there so won't be there if recycled
        ((ViewHolder) viewHolder).resetCardViewImage();
    }

    @Override
    public void onViewAttachedToWindow(Presenter.ViewHolder viewHolder) {
        //Log.d(TAG, "onViewAttachedToWindow");
    }

    public static class PicassoImageCardViewTarget implements Target {
        private ImageCardView mImageCardView;

        public PicassoImageCardViewTarget(ImageCardView mImageCardView) {
            this.mImageCardView = mImageCardView;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            Drawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
            mImageCardView.setMainImage(bitmapDrawable);
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            mImageCardView.setMainImage(drawable);
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {
            // Do nothing, default_background manager has its own transitions
        }
    }

}
