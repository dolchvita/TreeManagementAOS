package com.snd.app.ui.tree;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.snd.app.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    protected String TAG=this.getClass().getName();
    private List<Bitmap> imageList = new ArrayList<>();

    private MutableLiveData<Integer> _alertDialog=new MutableLiveData();

    // 사진 번호
    public Integer clickedPosition;
    private MutableLiveData<Integer> _tabClick;  // setter
    public LiveData tabClick=getTabClcick();    // getter(결과)


    public void setImageList(List<Bitmap> imageList) {
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
