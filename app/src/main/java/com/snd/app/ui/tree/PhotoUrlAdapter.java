package com.snd.app.ui.tree;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.snd.app.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoUrlAdapter extends RecyclerView.Adapter<PhotoUrlAdapter.PhotoViewHolder> {
    protected String TAG=this.getClass().getName();
    private List<String> imageList;
    private MutableLiveData<Integer> _alertDialog=new MutableLiveData();

    // 사진 번호
    public Integer clickedPosition;
    private MutableLiveData<Integer> _tabClick;  // setter
    public LiveData tabClick=getTabClcick();    // getter(결과)


    // 이미지 리스트 반영
    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
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
        String imageUrl = imageList.get(position);
        holder.bind(imageUrl);
    }


    @Override
    public int getItemCount() {
        Log.d(TAG,"** getItemCount호출 - 개수는?  **"+imageList.size());
        return imageList.size();
    }


    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        PhotoViewHolder(View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.img);
            photoImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);   // 비율 조정
        }
        public void bind(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(photoImageView.getContext())
                        .load(imageUrl)
                        .into(photoImageView);
            } else {
                photoImageView.setImageResource(R.drawable.ic_nfc_on);
            }
        }
    }



    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        String imageUrl = imageList.get(position);
        holder.bind(imageUrl);
        holder.photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = holder.getAdapterPosition();
                // 클릭이벤트
                setTabClick(clickedPosition);
            }
        });
    }


    // 1 클릭
    public void setAlertDialog(int clickedPosition, Boolean flag) {
        this.clickedPosition=clickedPosition;
        // 확인 버튼 눌렀다면
        if(flag){
            imageList.remove(clickedPosition);
            notifyDataSetChanged();
        }
    }


    public void setTabClick(Integer value){
        // 클릭된 수로 속성 변경하기
        _tabClick.setValue(value);
    }

    public LiveData getTabClcick(){
        // 여기서 아래 변수가 null로 뜬다..
        if (_tabClick==null){
            _tabClick=new MutableLiveData<Integer>();
        }
        // 결과값 반환해주는 메서드
        return this._tabClick;
    }

}
