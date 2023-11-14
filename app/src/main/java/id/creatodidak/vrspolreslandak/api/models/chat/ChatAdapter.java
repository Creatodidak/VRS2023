package id.creatodidak.vrspolreslandak.api.models.chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;
import java.util.Objects;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.BitmapUtils;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.ShareWaUtils;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final List<MChatdata> chatDataList;
    private final Context context;
    private final ChatAdapter.OnItemClickListener onItemClickListener;
    private final DBHelper dbHelper;

    public ChatAdapter(Context context, List<MChatdata> chatDataList, ChatAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.chatDataList = chatDataList;
        this.onItemClickListener = onItemClickListener;

        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balonchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MChatdata data = chatDataList.get(position);
        int pos = position;

        holder.baloonImgMsg.setVisibility(View.GONE);
        holder.baloonTextMsg.setVisibility(View.GONE);
        holder.baloonBotMsg.setVisibility(View.GONE);
        holder.btn.setVisibility(View.GONE);

        if (data.getType().equals("bot")) {
            holder.baloonBotMsg.setVisibility(View.VISIBLE);
        } else if (data.getType().equals("text")) {
            holder.baloonTextMsg.setVisibility(View.VISIBLE);
        } else if (data.getType().equals("image")) {
            holder.baloonImgMsg.setVisibility(View.VISIBLE);
        } else if (data.getType().equals("button")) {
            holder.btn.setVisibility(View.VISIBLE);
        }

        if (data.getType().equals("image")) {
            String[] ndata = data.getData().split("###");
            File file = null;
            if (ndata.length == 2) {
                file = new File(Objects.requireNonNull(Uri.parse(ndata[1]).getPath())).getAbsoluteFile();
            } else {
                file = new File(Objects.requireNonNull(Uri.parse(ndata[0]).getPath())).getAbsoluteFile();
            }
            Glide.with(context)
                    .load(file)
                    .placeholder(R.drawable.defaultimg)
                    .into(holder.imageImgMsg);

            String newDimendsi = BitmapUtils.rasio(file.getAbsolutePath());
            ConstraintSet cs = new ConstraintSet();
            cs.clone(holder.bingkai);
            cs.setDimensionRatio(R.id.imageImgMsg, newDimendsi);
            cs.applyTo(holder.bingkai);

            File finalFile = file;
            holder.imageImgMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BitmapUtils.expandImage(context, finalFile.getAbsolutePath(), "FOTO");
                }
            });

            File finalFile1 = file;
            holder.imageImgMsg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    CustomDialog.up(
                            context,
                            "Konfirmasi",
                            "Bagikan data ini?",
                            "BAGIKAN",
                            "BATAL",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("image/jpeg"); // Tipe konten adalah gambar JPEG
                                    Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", finalFile1);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                    context.startActivity(Intent.createChooser(shareIntent, "Bagikan gambar melalui..."));
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, true, false
                    ).show();
                    return true;
                }
            });
        } else if (data.getType().equals("button")) {
            holder.btn.setText(data.getData());
        } else {
            String[] isi = data.getData().split("###");
            if (isi.length == 1) {
                holder.userTextMsg.setText("Laporan");
                holder.isiTextMsg.setText(isi[0]);
            } else {
                holder.userTextMsg.setText(isi[0]);
                holder.isiTextMsg.setText(isi[1]);
            }
            holder.isiBotMsg.setText(data.getData());
        }

        if (data.getCreatedAt().contains(WaktuLokal.getTanggal())) {
            holder.timeBotMsg.setText(DateUtils.waktuchat(data.getCreatedAt()));
            holder.timeImgMsg.setText(DateUtils.waktuchat(data.getCreatedAt()));
            holder.timeTextMsg.setText(DateUtils.waktuchat(data.getCreatedAt()));
        } else {
            holder.timeBotMsg.setText(DateUtils.waktuchatkemaren(data.getCreatedAt()));
            holder.timeImgMsg.setText(DateUtils.waktuchatkemaren(data.getCreatedAt()));
            holder.timeTextMsg.setText(DateUtils.waktuchatkemaren(data.getCreatedAt()));
        }

        holder.loadingTextMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onResendClick(data.getData(), position, data.getId(), "text");
            }
        });

        holder.loadingImgMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onResendClick(data.getData(), position, data.getId(), "image");
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onBtnClick();
            }
        });
        holder.baloonTextMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CustomDialog.up(
                        context,
                        "Konfirmasi",
                        "Bagikan data ini?",
                        "BAGIKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                ShareWaUtils.shareTextToWhatsApp(context, holder.isiTextMsg.getText().toString());
                            }

                            @Override
                            public void onNegativeButtonClick(AlertDialog alert) {
                                alert.dismiss();
                            }
                        },
                        true, true, false
                ).show();
                return true;
            }
        });

        if (!data.getType().equals("bot") && !data.getType().equals("button")) {

            if (dbHelper.getActChatId(data.getChatId()).equals("LaporanCekEmbung") && data.getStatus().equals("PENDING")) {
                holder.statusImgMsg.setImageResource(R.drawable.baseline_cloud_off2_24);
                holder.statusTextMsg.setImageResource(R.drawable.baseline_cloud_off2_24);
                holder.loadingTextMsg.setVisibility(View.INVISIBLE);
                holder.loadingImgMsg.setVisibility(View.INVISIBLE);
            } else if (dbHelper.getActChatId(data.getChatId()).equals("LaporanKampanyeCegahKarhutla") && data.getStatus().equals("PENDING")) {
                holder.statusImgMsg.setImageResource(R.drawable.baseline_cloud_off2_24);
                holder.statusTextMsg.setImageResource(R.drawable.baseline_cloud_off2_24);
                holder.loadingTextMsg.setVisibility(View.INVISIBLE);
                holder.loadingImgMsg.setVisibility(View.INVISIBLE);
            } else if (data.getStatus().equals("PENDING")) {
                holder.statusImgMsg.setImageResource(R.drawable.baseline_cloud_off2_24);
                holder.statusTextMsg.setImageResource(R.drawable.baseline_cloud_off2_24);
                holder.loadingTextMsg.setVisibility(View.VISIBLE);
                holder.loadingImgMsg.setVisibility(View.VISIBLE);
            } else if (data.getStatus().equals("LOADING")) {
                Glide.with(context).asGif().load(R.drawable.loadings).into(holder.statusImgMsg);
                Glide.with(context).asGif().load(R.drawable.loadings).into(holder.statusTextMsg);
                Glide.with(context).asGif().load(R.drawable.loadings).into(holder.loadingTextMsg);
                Glide.with(context).asGif().load(R.drawable.loadings).into(holder.loadingImgMsg);
            } else if (data.getStatus().equals("SERVER")) {
                holder.statusImgMsg.setImageResource(R.drawable.server);
                holder.statusTextMsg.setImageResource(R.drawable.server);
                holder.loadingImgMsg.setVisibility(View.INVISIBLE);
                holder.loadingTextMsg.setVisibility(View.INVISIBLE);

            }
        }

    }

    @Override
    public int getItemCount() {
        return chatDataList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onBtnClick();

        void onResendClick(String data, int position, int id, String type);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userBotMsg;
        TextView isiBotMsg;
        TextView timeBotMsg;
        TextView userTextMsg;
        TextView isiTextMsg;
        TextView timeTextMsg;
        TextView userImgMsg;
        TextView timeImgMsg;
        ImageView statusTextMsg;
        ImageView statusImgMsg;
        ImageView imageImgMsg;
        ImageView loadingTextMsg;
        ImageView loadingImgMsg;
        ConstraintLayout baloonBotMsg;
        ConstraintLayout baloonTextMsg;
        ConstraintLayout baloonImgMsg;
        ConstraintLayout bingkai;
        Button btn;

        public ViewHolder(@NonNull View v) {
            super(v);
            userBotMsg = v.findViewById(R.id.userBotMsg);
            isiBotMsg = v.findViewById(R.id.isiBotMsg);
            timeBotMsg = v.findViewById(R.id.timeBotMsg);
            userTextMsg = v.findViewById(R.id.userTextMsg);
            isiTextMsg = v.findViewById(R.id.isiTextMsg);
            timeTextMsg = v.findViewById(R.id.timeTextMsg);
            userImgMsg = v.findViewById(R.id.userImgMsg);
            timeImgMsg = v.findViewById(R.id.timeImgMsg);
            statusTextMsg = v.findViewById(R.id.statusTextMsg);
            statusImgMsg = v.findViewById(R.id.statusImgMsg);
            imageImgMsg = v.findViewById(R.id.imageImgMsg);
            baloonBotMsg = v.findViewById(R.id.baloonBotMsg);
            baloonTextMsg = v.findViewById(R.id.baloonTextMsg);
            baloonImgMsg = v.findViewById(R.id.baloonImgMsg);
            btn = v.findViewById(R.id.botButton);
            bingkai = v.findViewById(R.id.bingkaifoto);
            loadingTextMsg = v.findViewById(R.id.loadingTextMsg);
            loadingImgMsg = v.findViewById(R.id.loadingImgMsg);
        }
    }
}
