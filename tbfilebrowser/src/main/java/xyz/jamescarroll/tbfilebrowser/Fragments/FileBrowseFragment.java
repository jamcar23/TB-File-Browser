package xyz.jamescarroll.tbfilebrowser.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
 * Created by jamescarroll on 10/4/16.
 */
public class FileBrowseFragment extends Fragment{
    public static final String TAG = "FileBrowseFragment.TAG";
    private OnItemSelected mListener;
    private RecyclerView mRVFiles;
    private Node<File> mCurrentNode;

    public FileBrowseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_node_rv, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCurrentNode = TreeManager.getCurrentNode(FileBrowser.TREE_KEY);
        mRVFiles = (RecyclerView) view.findViewById(R.id.rv_files);

        if (mRVFiles != null) {
            mRVFiles.setAdapter(new FilesViewAdapter());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelected) {
            mListener = (OnItemSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateCurrentNode(int pos) {
        Node<File> n = pos == 0 && mCurrentNode.hasParent() ? mCurrentNode.getParent() :
                mCurrentNode.getChild(pos);

        if (n != null) {
            if (n.isLeaf()) {
                mListener.onFileSelected(n.getData());
            } else {
                mCurrentNode = n;
                TreeManager.setCurrentNode(FileBrowser.TREE_KEY, mCurrentNode);
                mRVFiles.getAdapter().notifyDataSetChanged();
            }
        } else {
            Log.e(TAG, "updateCurrentNode: n is null");
        }
    }

    public interface OnItemSelected {
        void onFileSelected(File file);
    }

    public static FileBrowseFragment findFragment(AppCompatActivity activity) {
        FileBrowseFragment f = (FileBrowseFragment) activity.getSupportFragmentManager()
                .findFragmentByTag(TAG);

        if (f == null) {
            f = new FileBrowseFragment();
        }

        return f;
    }

    public class FilesViewAdapter extends RecyclerView.Adapter<FilesViewAdapter.ViewHolder> {


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

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                mTextView = (TextView) itemView.findViewById(R.id.content);
            }

            private void updateUi(Node node) {
                mTextView.setText(node.getName());
            }

            @Override
            public void onClick(View v) {
                updateCurrentNode(getAdapterPosition());

            }
        }

    }
}
