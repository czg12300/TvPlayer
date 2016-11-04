package com.jake.simple;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * description：
 *
 * @author Administrator
 * @since 2016/10/31 21:46
 */


public class TvListActivity extends AppCompatActivity {
    private MyAdapter mAdapter;

    public static void start(@NonNull Context context) {
        Intent it = new Intent(context, TvListActivity.class);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("电视");
        ListView listView = new ListView(this);
        setContentView(listView);
        mAdapter = new MyAdapter(this);
        mAdapter.set(getDatas());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemInfo info = mAdapter.getItem(position);
                TvActivity.start(TvListActivity.this, info.url);
            }
        });
    }


    private List<ItemInfo> getDatas() {
        List<ItemInfo> infos = new ArrayList<>();
        infos.add(new ItemInfo("CCTV1综合", "http://106.36.45.36/live.aishang.ctlcdn.com/00000110240001_1/encoder/1/playlist.m3u8"));
        infos.add(new ItemInfo("CCTV1综合", " http://58.135.196.138:8090/live/db3bd108e3364bf3888ccaf8377af077/index.m3u8"));
        infos.add(new ItemInfo("CCTV2财经", "http://58.135.196.138:8090/live/e31fa63612644555a545781ea32e66d4/index.m3u8"));
        infos.add(new ItemInfo("CCTV3综艺 ", "http://58.135.196.138:8090/live/A68CE6833D654a9e932A657689463088/index.m3u8"));
        infos.add(new ItemInfo("CCTV4中文国际(亚洲)", "http://58.135.196.138:8090/live/56383AB184D54ac8B20478B6A43906DC/index.m3u8"));
        infos.add(new ItemInfo("cctv5体育  ", "  http://58.135.196.138:8090/live/6b9e3889ec6e2ab1a8c7bd0845e5368a/index.m3u8"));
        infos.add(new ItemInfo("CCTV6电影  ", "  http://58.135.196.138:8090/live/6132e9cb136050bd94822db31d1401af/index.m3u8"));
        infos.add(new ItemInfo("CCTV7军事农业", " http://58.135.196.138:8090/live/a9a97bed07eca008a1c88c9d7c74965d/index.m3u8"));
        infos.add(new ItemInfo("CCTV8电视剧", " http://58.135.196.138:8090/live/6e38d69416f160bad4657788d6c06c01/index.m3u8"));
        infos.add(new ItemInfo("CCTV9纪录", " http://58.135.196.138:8090/live/557a950d2cfcf2ee1aad5a260893c2b8/index.m3u8"));
        infos.add(new ItemInfo("CCTV10科教", " http://58.135.196.138:8090/live/a0effe8f31af0011b750e817c1b6e8c7/index.m3u8"));
        infos.add(new ItemInfo("CCTV11戏曲", " http://58.135.196.138:8090/live/2D5B4B7AB5A14d79A0D9A1D37540E2BF/index.m3u8"));
        infos.add(new ItemInfo("CCTV12社会与法", " http://58.135.196.138:8090/live/3720126fbea745f74c1c89df9797caac/index.m3u8"));
        infos.add(new ItemInfo("CCTV13新闻", " http://58.135.196.138:8090/live/5e02ac2a0829f7ab10d6543fdd211d8e/index.m3u8"));
        infos.add(new ItemInfo("CCTV14少儿", " http://58.135.196.138:8090/live/4A5DF0BA0C994081B02F8215491C4E48/index.m3u8"));
        infos.add(new ItemInfo(" CCTV15音乐", " http://58.135.196.138:8090/live/ADBD55B50F2D47bb970DCCBAF458E6C8/index.m3u8"));
        infos.add(new ItemInfo("发现之旅", " http://58.135.196.138:8090/live/1AB9A2A4F4CE48b397D72A067906D763/index.m3u8"));
        infos.add(new ItemInfo("风云足球", " http://58.135.196.138:8090/live/D6C05AA9AFEF4ac58A6ED0FFF27EE85A/index.m3u8"));
        infos.add(new ItemInfo(" 第一剧场", "http://58.135.196.138:8090/live/13BCA1A2EFDB4db3B5DF6A8D343C3E39/index.m3u8"));
        infos.add(new ItemInfo(" 怀旧剧场", " http://58.135.196.138:8090/live/65A216E9B6FC4905AF6756E2AEFC3ED3/index.m3u8"));
        infos.add(new ItemInfo(" 风云剧场 ", "http://58.135.196.138:8090/live/8D574FF6D0B04a0eA8B51D55B7C8E6DD/index.m3u8"));
        infos.add(new ItemInfo(" 凤凰中文", " http://58.135.196.138:8090/live/789E9CE292A040d2A459CF55D2FB362E/789E9CE292A040d2A459CF55D2FB362E.m3u8"));
        infos.add(new ItemInfo(" 凤凰资讯", " http://58.135.196.138:8090/live/369024B4C9BF4b0f909187FC58B9951C/369024B4C9BF4b0f909187FC58B9951C.m3u8"));
        infos.add(new ItemInfo(" 北京文艺 ", "http://58.135.196.138:8090/live/F56C438F495049168C447030C175F7DB/F56C438F495049168C447030C175F7DB.m3u8"));
        infos.add(new ItemInfo(" 北京科教", " http://58.135.196.138:8090/live/1E8AF3D32194426dAC087FF20B098BCC/1E8AF3D32194426dAC087FF20B098BCC.m3u8"));
        infos.add(new ItemInfo(" 北京影视", " http://58.135.196.138:8090/live/8a6870a0febf6283b27210aab721beb4/8a6870a0febf6283b27210aab721beb4.m3u8"));
        infos.add(new ItemInfo(" 北京财经", " http://58.135.196.138:8090/live/8C213826A5E049849097FE77230B0616/8C213826A5E049849097FE77230B0616.m3u8"));
        infos.add(new ItemInfo(" 北京体育", " http://58.135.196.138:8090/live/c6e9431fbe2634418def86d4b193d80d/c6e9431fbe2634418def86d4b193d80d.m3u8"));
        infos.add(new ItemInfo(" 北京生活", " http://58.135.196.138:8090/live/dd728b0bef32c98060a0d4f757b6dbaf/dd728b0bef32c98060a0d4f757b6dbaf.m3u8"));
        infos.add(new ItemInfo("北京青年", " http://58.135.196.138:8090/live/87446EA70C0C42119656A11938ACB2DD/87446EA70C0C42119656A11938ACB2DD.m3u8"));
        infos.add(new ItemInfo("北京新闻", " http://58.135.196.138:8090/live/C42481BFB2FD456480A098436834DFF0/C42481BFB2FD456480A098436834DFF0.m3u8"));
        infos.add(new ItemInfo("卡酷少儿", " http://58.135.196.138:8090/live/543E9B8D048648e399E66EA338EF761C/543E9B8D048648e399E66EA338EF761C.m3u8"));
        infos.add(new ItemInfo(" DOXTV ", "http://58.135.196.138:8090/live/7FDF7FBEC2B8474dA349102809CB8F97/7FDF7FBEC2B8474dA349102809CB8F97.m3u863"));
        infos.add(new ItemInfo("车迷频道", " http://58.135.196.138:8090/live/022ED5AEC04546d99958328E2E6BB614/022ED5AEC04546d99958328E2E6BB614.m3u8"));
        infos.add(new ItemInfo("电子体育", " http://58.135.196.138:8090/live/A3A45E102C5F41afBD78A5E27FF0578F/A3A45E102C5F41afBD78A5E27FF0578F.m3u8"));
        infos.add(new ItemInfo("财富天下", " http://58.135.196.138:8090/live/2615D9C21AB248678ED9CF172E463539/2615D9C21AB248678ED9CF172E463539.m3u8"));
        infos.add(new ItemInfo("东方财经", " http://58.135.196.138:8090/live/295A4BAF84504aba8B1ED913A9790E08/295A4BAF84504aba8B1ED913A9790E08.m3u8"));
        infos.add(new ItemInfo("动漫秀场", " http://58.135.196.138:8090/live/E1F31105D6094bb6ADB5E698CB6EE4D8/E1F31105D6094bb6ADB5E698CB6EE4D8.m3u8"));
        infos.add(new ItemInfo("读书频道", " http://58.135.196.138:8090/live/6E6AAB7CE02545e0868A71B4A8DA2559/6E6AAB7CE02545e0868A71B4A8DA2559.m3u8"));
        infos.add(new ItemInfo("法治天地", " http://58.135.196.138:8090/live/4818B652431344a8882ED935471711B5/4818B652431344a8882ED935471711B5.m3u8"));
        infos.add(new ItemInfo("高尔夫", " http://58.135.196.138:8090/live/ED54356140BD4282AA0C6FE656CC6583/ED54356140BD4282AA0C6FE656CC6583.m3u8"));
        infos.add(new ItemInfo("欢笑剧场", " http://58.135.196.138:8090/live/B081DB925E434fa8A8A2812860B86785/B081DB925E434fa8A8A2812860B86785.m3u8"));
        infos.add(new ItemInfo("环球旅游 ", "http://58.135.196.138:8090/live/B3EDABA59F6240889F4C5162AF4655D3/B3EDABA59F6240889F4C5162AF4655D3.m3u8"));
        infos.add(new ItemInfo("环球奇观 ", "http://58.135.196.138:8090/live/333BAD8D3C2E4bfeA648053B7286A0BF/333BAD8D3C2E4bfeA648053B7286A0BF.m3u8"));
        infos.add(new ItemInfo(" 极速汽车", " http://58.135.196.138:8090/live/77C9E60036F64711B8C5F5111BF5A7F1/77C9E60036F64711B8C5F5111BF5A7F1.m3u8"));
        infos.add(new ItemInfo(" 家庭理财", " http://58.135.196.138:8090/live/D70719FBFE2242c6BA2B6235C834D896/D70719FBFE2242c6BA2B6235C834D896.m3u8"));
        infos.add(new ItemInfo(" 金鹰卡通", " http://58.135.196.138:8090/live/345A6019C2734ec7BE05BD4C6837EFD3/345A6019C2734ec7BE05BD4C6837EFD3.m3u8"));
        infos.add(new ItemInfo("  劲爆体育 ", "http://58.135.196.138:8090/live/5B33459CF0314e3e831351B3ED17F844/5B33459CF0314e3e831351B3ED17F844.m3u8"));
        infos.add(new ItemInfo("  考试在线 ", "http://58.135.196.138:8090/live/AA4E088E6AB54779A3A4ED856016317E/AA4E088E6AB54779A3A4ED856016317E.m3u8"));
        infos.add(new ItemInfo(" 快乐宠物", " http://58.135.196.138:8090/live/95589A5033F948749F9ABD897E2C0032/95589A5033F948749F9ABD897E2C0032.m3u8"));
        infos.add(new ItemInfo(" 欧洲足球", "http://58.135.196.138:8090/live/980D3B2CF76042269F81A12B635ED9E9/980D3B2CF76042269F81A12B635ED9E9.m3u8"));
        infos.add(new ItemInfo("  全纪实", "http://58.135.196.138:8090/live/94C1846FCB3E48278D2B6BA49164EC33/94C1846FCB3E48278D2B6BA49164EC33.m3u8"));
        infos.add(new ItemInfo("  时代出行", "http://58.135.196.138:8090/live/379B386B5A214f8fA2B9F4DF1946BF6D/379B386B5A214f8fA2B9F4DF1946BF6D.m3u8"));
        infos.add(new ItemInfo("  时代风尚 ", "http://58.135.196.138:8090/live/373331B772D1418eAB2D7EEA0C7B3691/373331B772D1418eAB2D7EEA0C7B3691.m3u8"));
        infos.add(new ItemInfo("  时代美食", " http://58.135.196.138:8090/live/07A1BF033E7940898A01053F79EF3595/07A1BF033E7940898A01053F79EF3595.m3u8"));
        infos.add(new ItemInfo(" 收藏天下", " http://58.135.196.138:8090/live/91ADF5DB36DE4c0f8E56FC3386836EA3/91ADF5DB36DE4c0f8E56FC3386836EA3.m3u8"));
        infos.add(new ItemInfo(" 四海钓鱼 ", "http://58.135.196.138:8090/live/6E1087643B3D445fBCECAF3B54544767/6E1087643B3D445fBCECAF3B54544767.m3u8"));
        infos.add(new ItemInfo(" 先锋纪录 ", "http://58.135.196.138:8090/live/394BAD414F284fbf9EA8F08106741A63/394BAD414F284fbf9EA8F08106741A63.m3u8"));
        infos.add(new ItemInfo(" 新动漫 ", "http://58.135.196.138:8090/live/3CDBE7F96BC1456cAAD86C571D5B1C43/3CDBE7F96BC1456cAAD86C571D5B1C43.m3u8"));
        infos.add(new ItemInfo(" 新娱乐 ", "http://58.135.196.138:8090/live/883091E42C394ec3996932F152B95FD3/883091E42C394ec3996932F152B95FD3.m3u8"));
        infos.add(new ItemInfo(" 弈坛春秋", " http://58.135.196.138:8090/live/62E9275D131549b080F9ABBDB5E48F4E/62E9275D131549b080F9ABBDB5E48F4E.m3u8"));
        infos.add(new ItemInfo("优优宝贝 ", "http://58.135.196.138:8090/live/FF453B3E5B4F446e978A7624254E3CD2/FF453B3E5B4F446e978A7624254E3CD2.m3u8"));
        infos.add(new ItemInfo("  游戏风云 ", "  http://58.135.196.138:8090/live/82769FB7307D460193359CA465D1C159/82769FB7307D460193359CA465D1C159.m3u8"));
        infos.add(new ItemInfo(" 京视剧场 ", "   http://58.135.196.138:8090/live/369B8B9031894010B5BA822A1637FEF1/369B8B9031894010B5BA822A1637FEF1.m3u8"));
        return infos;
    }

    private static class ItemInfo {
        public String name;
        public String url;
        public String icon;

        public ItemInfo(String name, String url) {
            this.name = name;
            this.icon = "http://p1.img.cctvpic.com/fmspic/pd/1000450cctv20160922.jpg";
            this.url = url;
        }

        public ItemInfo(String name, String icon, String url) {
            this.name = name;
            this.icon = icon;
            this.url = url;
        }
    }

    private static class MyAdapter extends BaseListAdapter<ItemInfo, MyAdapter.ViewHolder> {
        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ItemInfo info = getItem(position);
            holder.tvName.setText(info.name);
            if (!TextUtils.isEmpty(info.icon)) {
                Glide.with(mContext).load(info.icon).into(holder.ivIcon);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(mContext, R.layout.item_video, null));
        }

        static class ViewHolder extends BaseViewHolder {
            TextView tvName;
            ImageView ivIcon;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) findViewById(R.id.tv_name);
                ivIcon = (ImageView) findViewById(R.id.iv_icon);
            }
        }
    }
}
