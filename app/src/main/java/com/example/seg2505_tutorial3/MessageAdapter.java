package com.example.seg2505_tutorial3;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptateur pour la RecyclerView qui affiche les messages.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    /**
     * Ajoute un nouveau message à la liste et notifie l'adaptateur.
     *
     * @param message Le message à ajouter.
     */
    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    /**
     * Met à jour un message existant dans la liste et notifie l'adaptateur.
     *
     * @param message Le message mis à jour.
     */
    public void updateMessage(Message message) {
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).key.equals(message.key)) {
                messageList.set(i, message);
                notifyItemChanged(i);
                return;
            }
        }
        // Si le message n'existe pas, l'ajouter
        addMessage(message);
    }

    /**
     * Supprime un message de la liste et notifie l'adaptateur.
     *
     * @param messageKey La clé du message à supprimer.
     */
    public void removeMessage(String messageKey) {
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).key.equals(messageKey)) {
                messageList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infle le layout de l'item de message
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Lie les données au ViewHolder
        Message message = messageList.get(position);
        holder.textViewAuthor.setText(message.author);
        holder.textViewMessageText.setText(message.text);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * Classe interne pour le ViewHolder du message.
     */
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor;
        TextView textViewMessageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewMessageText = itemView.findViewById(R.id.textViewMessageText);
        }
    }
}
