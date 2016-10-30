/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tv.danmaku.ijk.media.example.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import tv.danmaku.ijk.media.example.R;
import tv.danmaku.ijk.media.example.activities.VideoActivity;

public class SampleMediaListFragment extends Fragment {
    private ListView mFileListView;
    private SampleMediaAdapter mAdapter;

    public static SampleMediaListFragment newInstance() {
        SampleMediaListFragment f = new SampleMediaListFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_file_list, container, false);
        mFileListView = (ListView) viewGroup.findViewById(R.id.file_list_view);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        mAdapter = new SampleMediaAdapter(activity);
        mFileListView.setAdapter(mAdapter);
        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                SampleMediaItem item = mAdapter.getItem(position);
                String name = item.mName;
                String url = item.mUrl;
                VideoActivity.intentTo(activity, url, name);
            }
        });
        mAdapter.addItem("CCTV1综合", " http://58.135.196.138:8090/live/db3bd108e3364bf3888ccaf8377af077/index.m3u8");
        mAdapter.addItem("CCTV2财经", "http://58.135.196.138:8090/live/e31fa63612644555a545781ea32e66d4/index.m3u8");
        mAdapter.addItem("CCTV3综艺 ", "http://58.135.196.138:8090/live/A68CE6833D654a9e932A657689463088/index.m3u8");
        mAdapter.addItem("CCTV4中文国际(亚洲)", "http://58.135.196.138:8090/live/56383AB184D54ac8B20478B6A43906DC/index.m3u8");
        mAdapter.addItem("cctv5体育  ", "  http://58.135.196.138:8090/live/6b9e3889ec6e2ab1a8c7bd0845e5368a/index.m3u8");
        mAdapter.addItem("CCTV6电影  ", "  http://58.135.196.138:8090/live/6132e9cb136050bd94822db31d1401af/index.m3u8");
        mAdapter.addItem("CCTV7军事农业", " http://58.135.196.138:8090/live/a9a97bed07eca008a1c88c9d7c74965d/index.m3u8");
        mAdapter.addItem("CCTV8电视剧", " http://58.135.196.138:8090/live/6e38d69416f160bad4657788d6c06c01/index.m3u8");
        mAdapter.addItem("CCTV9纪录", " http://58.135.196.138:8090/live/557a950d2cfcf2ee1aad5a260893c2b8/index.m3u8");
        mAdapter.addItem("CCTV10科教", " http://58.135.196.138:8090/live/a0effe8f31af0011b750e817c1b6e8c7/index.m3u8");
        mAdapter.addItem("CCTV11戏曲", " http://58.135.196.138:8090/live/2D5B4B7AB5A14d79A0D9A1D37540E2BF/index.m3u8");
        mAdapter.addItem("CCTV12社会与法", " http://58.135.196.138:8090/live/3720126fbea745f74c1c89df9797caac/index.m3u8");
        mAdapter.addItem("CCTV13新闻", " http://58.135.196.138:8090/live/5e02ac2a0829f7ab10d6543fdd211d8e/index.m3u8");
        mAdapter.addItem("CCTV14少儿", " http://58.135.196.138:8090/live/4A5DF0BA0C994081B02F8215491C4E48/index.m3u8");
        mAdapter.addItem(" CCTV15音乐", " http://58.135.196.138:8090/live/ADBD55B50F2D47bb970DCCBAF458E6C8/index.m3u8");
        mAdapter.addItem("发现之旅", " http://58.135.196.138:8090/live/1AB9A2A4F4CE48b397D72A067906D763/index.m3u8");
        mAdapter.addItem("风云足球", " http://58.135.196.138:8090/live/D6C05AA9AFEF4ac58A6ED0FFF27EE85A/index.m3u8");
        mAdapter.addItem(" 第一剧场", "http://58.135.196.138:8090/live/13BCA1A2EFDB4db3B5DF6A8D343C3E39/index.m3u8");
        mAdapter.addItem(" 怀旧剧场", " http://58.135.196.138:8090/live/65A216E9B6FC4905AF6756E2AEFC3ED3/index.m3u8");
        mAdapter.addItem(" 风云剧场 ", "http://58.135.196.138:8090/live/8D574FF6D0B04a0eA8B51D55B7C8E6DD/index.m3u8");
        mAdapter.addItem(" 凤凰中文", " http://58.135.196.138:8090/live/789E9CE292A040d2A459CF55D2FB362E/789E9CE292A040d2A459CF55D2FB362E.m3u8");
        mAdapter.addItem(" 凤凰资讯", " http://58.135.196.138:8090/live/369024B4C9BF4b0f909187FC58B9951C/369024B4C9BF4b0f909187FC58B9951C.m3u8");
        mAdapter.addItem(" 北京文艺 ", "http://58.135.196.138:8090/live/F56C438F495049168C447030C175F7DB/F56C438F495049168C447030C175F7DB.m3u8");
        mAdapter.addItem(" 北京科教", " http://58.135.196.138:8090/live/1E8AF3D32194426dAC087FF20B098BCC/1E8AF3D32194426dAC087FF20B098BCC.m3u8");
        mAdapter.addItem(" 北京影视", " http://58.135.196.138:8090/live/8a6870a0febf6283b27210aab721beb4/8a6870a0febf6283b27210aab721beb4.m3u8");
        mAdapter.addItem(" 北京财经", " http://58.135.196.138:8090/live/8C213826A5E049849097FE77230B0616/8C213826A5E049849097FE77230B0616.m3u8");
        mAdapter.addItem(" 北京体育", " http://58.135.196.138:8090/live/c6e9431fbe2634418def86d4b193d80d/c6e9431fbe2634418def86d4b193d80d.m3u8");
        mAdapter.addItem(" 北京生活", " http://58.135.196.138:8090/live/dd728b0bef32c98060a0d4f757b6dbaf/dd728b0bef32c98060a0d4f757b6dbaf.m3u8");
        mAdapter.addItem("北京青年", " http://58.135.196.138:8090/live/87446EA70C0C42119656A11938ACB2DD/87446EA70C0C42119656A11938ACB2DD.m3u8");
        mAdapter.addItem("北京新闻", " http://58.135.196.138:8090/live/C42481BFB2FD456480A098436834DFF0/C42481BFB2FD456480A098436834DFF0.m3u8");
        mAdapter.addItem("卡酷少儿", " http://58.135.196.138:8090/live/543E9B8D048648e399E66EA338EF761C/543E9B8D048648e399E66EA338EF761C.m3u8");
        mAdapter.addItem(" DOXTV ", "http://58.135.196.138:8090/live/7FDF7FBEC2B8474dA349102809CB8F97/7FDF7FBEC2B8474dA349102809CB8F97.m3u863");
        mAdapter.addItem("车迷频道", " http://58.135.196.138:8090/live/022ED5AEC04546d99958328E2E6BB614/022ED5AEC04546d99958328E2E6BB614.m3u8");
        mAdapter.addItem("电子体育", " http://58.135.196.138:8090/live/A3A45E102C5F41afBD78A5E27FF0578F/A3A45E102C5F41afBD78A5E27FF0578F.m3u8");
        mAdapter.addItem("财富天下", " http://58.135.196.138:8090/live/2615D9C21AB248678ED9CF172E463539/2615D9C21AB248678ED9CF172E463539.m3u8");
        mAdapter.addItem("东方财经", " http://58.135.196.138:8090/live/295A4BAF84504aba8B1ED913A9790E08/295A4BAF84504aba8B1ED913A9790E08.m3u8");
        mAdapter.addItem("动漫秀场", " http://58.135.196.138:8090/live/E1F31105D6094bb6ADB5E698CB6EE4D8/E1F31105D6094bb6ADB5E698CB6EE4D8.m3u8");
        mAdapter.addItem("读书频道", " http://58.135.196.138:8090/live/6E6AAB7CE02545e0868A71B4A8DA2559/6E6AAB7CE02545e0868A71B4A8DA2559.m3u8");
        mAdapter.addItem("法治天地", " http://58.135.196.138:8090/live/4818B652431344a8882ED935471711B5/4818B652431344a8882ED935471711B5.m3u8");
        mAdapter.addItem("高尔夫", " http://58.135.196.138:8090/live/ED54356140BD4282AA0C6FE656CC6583/ED54356140BD4282AA0C6FE656CC6583.m3u8");
        mAdapter.addItem("欢笑剧场", " http://58.135.196.138:8090/live/B081DB925E434fa8A8A2812860B86785/B081DB925E434fa8A8A2812860B86785.m3u8");
        mAdapter.addItem("环球旅游 ", "http://58.135.196.138:8090/live/B3EDABA59F6240889F4C5162AF4655D3/B3EDABA59F6240889F4C5162AF4655D3.m3u8");
        mAdapter.addItem("环球奇观 ", "http://58.135.196.138:8090/live/333BAD8D3C2E4bfeA648053B7286A0BF/333BAD8D3C2E4bfeA648053B7286A0BF.m3u8");
        mAdapter.addItem(" 极速汽车", " http://58.135.196.138:8090/live/77C9E60036F64711B8C5F5111BF5A7F1/77C9E60036F64711B8C5F5111BF5A7F1.m3u8");
        mAdapter.addItem(" 家庭理财", " http://58.135.196.138:8090/live/D70719FBFE2242c6BA2B6235C834D896/D70719FBFE2242c6BA2B6235C834D896.m3u8");
        mAdapter.addItem(" 金鹰卡通", " http://58.135.196.138:8090/live/345A6019C2734ec7BE05BD4C6837EFD3/345A6019C2734ec7BE05BD4C6837EFD3.m3u8");
        mAdapter.addItem("  劲爆体育 ", "http://58.135.196.138:8090/live/5B33459CF0314e3e831351B3ED17F844/5B33459CF0314e3e831351B3ED17F844.m3u8");
        mAdapter.addItem("  考试在线 ", "http://58.135.196.138:8090/live/AA4E088E6AB54779A3A4ED856016317E/AA4E088E6AB54779A3A4ED856016317E.m3u8");
        mAdapter.addItem(" 快乐宠物", " http://58.135.196.138:8090/live/95589A5033F948749F9ABD897E2C0032/95589A5033F948749F9ABD897E2C0032.m3u8");
        mAdapter.addItem(" 欧洲足球", "http://58.135.196.138:8090/live/980D3B2CF76042269F81A12B635ED9E9/980D3B2CF76042269F81A12B635ED9E9.m3u8");
        mAdapter.addItem("  全纪实", "http://58.135.196.138:8090/live/94C1846FCB3E48278D2B6BA49164EC33/94C1846FCB3E48278D2B6BA49164EC33.m3u8");
        mAdapter.addItem("  时代出行", "http://58.135.196.138:8090/live/379B386B5A214f8fA2B9F4DF1946BF6D/379B386B5A214f8fA2B9F4DF1946BF6D.m3u8");
        mAdapter.addItem("  时代风尚 ", "http://58.135.196.138:8090/live/373331B772D1418eAB2D7EEA0C7B3691/373331B772D1418eAB2D7EEA0C7B3691.m3u8");
        mAdapter.addItem("  时代美食", " http://58.135.196.138:8090/live/07A1BF033E7940898A01053F79EF3595/07A1BF033E7940898A01053F79EF3595.m3u8");
        mAdapter.addItem(" 收藏天下", " http://58.135.196.138:8090/live/91ADF5DB36DE4c0f8E56FC3386836EA3/91ADF5DB36DE4c0f8E56FC3386836EA3.m3u8");
        mAdapter.addItem(" 四海钓鱼 ", "http://58.135.196.138:8090/live/6E1087643B3D445fBCECAF3B54544767/6E1087643B3D445fBCECAF3B54544767.m3u8");
        mAdapter.addItem(" 先锋纪录 ", "http://58.135.196.138:8090/live/394BAD414F284fbf9EA8F08106741A63/394BAD414F284fbf9EA8F08106741A63.m3u8");
        mAdapter.addItem(" 新动漫 ", "http://58.135.196.138:8090/live/3CDBE7F96BC1456cAAD86C571D5B1C43/3CDBE7F96BC1456cAAD86C571D5B1C43.m3u8");
        mAdapter.addItem(" 新娱乐 ", "http://58.135.196.138:8090/live/883091E42C394ec3996932F152B95FD3/883091E42C394ec3996932F152B95FD3.m3u8");
        mAdapter.addItem(" 弈坛春秋", " http://58.135.196.138:8090/live/62E9275D131549b080F9ABBDB5E48F4E/62E9275D131549b080F9ABBDB5E48F4E.m3u8");
        mAdapter.addItem("优优宝贝 ", "http://58.135.196.138:8090/live/FF453B3E5B4F446e978A7624254E3CD2/FF453B3E5B4F446e978A7624254E3CD2.m3u8");
        mAdapter.addItem("  游戏风云 ", "  http://58.135.196.138:8090/live/82769FB7307D460193359CA465D1C159/82769FB7307D460193359CA465D1C159.m3u8");
        mAdapter.addItem(" 京视剧场 ", "   http://58.135.196.138:8090/live/369B8B9031894010B5BA822A1637FEF1/369B8B9031894010B5BA822A1637FEF1.m3u8");
        mAdapter.addItem("http://58.135.196.138:8090/live/db3bd108e3364bf3888ccaf8377af077/index.m3u8", "bipbop basic master playlist");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8", "bipbop basic 400x300 @ 232 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8", "bipbop basic 640x480 @ 650 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear3/prog_index.m3u8", "bipbop basic 640x480 @ 1 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear4/prog_index.m3u8", "bipbop basic 960x720 @ 2 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear0/prog_index.m3u8", "bipbop basic 22.050Hz stereo @ 40 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8", "bipbop advanced master playlist");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear1/prog_index.m3u8", "bipbop advanced 416x234 @ 265 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear2/prog_index.m3u8", "bipbop advanced 640x360 @ 580 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear3/prog_index.m3u8", "bipbop advanced 960x540 @ 910 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear4/prog_index.m3u8", "bipbop advanced 1289x720 @ 1 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear5/prog_index.m3u8", "bipbop advanced 1920x1080 @ 2 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear0/prog_index.m3u8", "bipbop advanced 22.050Hz stereo @ 40 kbps");
    }

    final class SampleMediaItem {
        String mUrl;
        String mName;

        public SampleMediaItem(String url, String name) {
            mUrl = url.trim();
            mName = name.trim();
        }
    }

    final class SampleMediaAdapter extends ArrayAdapter<SampleMediaItem> {
        public SampleMediaAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
        }

        public void addItem(String url, String name) {
            add(new SampleMediaItem(name, url));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.mNameTextView = (TextView) view.findViewById(android.R.id.text1);
                viewHolder.mUrlTextView = (TextView) view.findViewById(android.R.id.text2);
            }

            SampleMediaItem item = getItem(position);
            viewHolder.mNameTextView.setText(item.mName);
//            viewHolder.mUrlTextView.setText(item.mUrl.trim());

            return view;
        }

        final class ViewHolder {
            public TextView mNameTextView;
            public TextView mUrlTextView;
        }
    }
}
