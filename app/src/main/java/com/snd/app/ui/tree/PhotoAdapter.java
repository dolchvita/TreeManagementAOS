package com.snd.app.ui.tree;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.snd.app.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    protected String TAG=this.getClass().getName();
    private List<Bitmap> imageList = new ArrayList<>();

    public void setImageList(List<Bitmap> imageList) {
        Log.d(TAG,"** setImageList 호출 ** "+imageList);

        this.imageList = imageList;
        notifyDataSetChanged();     // 리스트 갱신
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 레이아웃을 새롭게 생성한다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);

        int width = 240;  // 원하는 너비
        int height = 240; // 원하는 높이
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        view.setLayoutParams(layoutParams);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Bitmap photo = imageList.get(position);
        holder.bind(photo);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"** getItemCount 호출 **");

        return imageList.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        PhotoViewHolder(View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.img);

            photoImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);   // 비율 조정
        }


        public void bind(Bitmap image) {
            if (image != null) {
                // ImageView에 이미지를 바인딩합니다.
               photoImageView.setImageBitmap(image);

            } else {
                // 이미지가 null인 경우 기본 이미지 또는 오류 이미지 등을 설정할 수 있습니다.
                // 예시:
                photoImageView.setImageResource(R.drawable.ic_nfc_on);
                // 또는
                // photoImageView.setImageDrawable(ContextCompat.getDrawable(photoImageView.getContext(), R.drawable.default_image));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        Bitmap photoBitmap = imageList.get(position);
        holder.bind(photoBitmap);
        //holder.photoImageView.setImageBitmap(photoBitmap);
    }


}
