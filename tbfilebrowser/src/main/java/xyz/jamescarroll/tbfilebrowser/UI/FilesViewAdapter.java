package xyz.jamescarroll.tbfilebrowser.UI;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

import xyz.jamescarroll.tbfilebrowser.FileBrowser;
import xyz.jamescarroll.tbfilebrowser.Node.Node;
import xyz.jamescarroll.tbfilebrowser.R;
import xyz.jamescarroll.tbfilebrowser.TreeManager;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 James Carroll
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class FilesViewAdapter extends RecyclerView.Adapter<FilesViewAdapter.ViewHolder> {
    private static final String TAG = "FilesViewAdapter.TAG";
    private Node<File> mCurrentNode;
    private OnItemSelected mListener;

    public FilesViewAdapter(Node<File> mCurrentNode) {
        this.mCurrentNode = mCurrentNode;
    }

    public FilesViewAdapter(Node<File> mCurrentNode, OnItemSelected mListener) {
        this.mCurrentNode = mCurrentNode;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_node,
                parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mCurrentNode.hasParent()) {
            if (position == 0) {
                holder.mTextView.setText("../");
            } else {
                holder.updateUi(mCurrentNode.getChild(position - 1));
            }
        } else {
            holder.updateUi(mCurrentNode.getChild(position));
        }
    }

    @Override
    public int getItemCount() {
        int s = mCurrentNode.getChildren().size();
        return mCurrentNode.hasParent() ? s + 1 : s;
    }

    public OnItemSelected getListener() {
        return mListener;
    }

    public void setListener(OnItemSelected mListener) {
        this.mListener = mListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            setUpUi();
        }

        protected void setUpUi() {
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView.findViewById(R.id.content);
        }

        protected void updateUi(Node node) {
            mTextView.setText(node.getName());
        }

        @Override
        public void onClick(View v) {
            final int pos = getAdapterPosition();
            Node<File> n = pos == 0 && mCurrentNode.hasParent() ? mCurrentNode.getParent() :
                    mCurrentNode.getChild(pos);

            if (n != null) {
                if (n.isLeaf() && mListener != null) {
                    mListener.onFileSelected(n.getData());
                } else {
                    mCurrentNode = n;
                    TreeManager.setCurrentNode(FileBrowser.TREE_KEY, mCurrentNode);
                    FilesViewAdapter.this.notifyDataSetChanged();
                }
            } else {
                Log.e(TAG, "updateCurrentNode: n is null");
            }

        }
    }

    public interface OnItemSelected {
        void onFileSelected(File file);
    }

}
