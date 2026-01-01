package com.example.traveling_app.common;

import com.example.traveling_app.model.tour.Discount;

public class LoadVoucher {
    public static void load(){
        Discount voucher1 = new Discount("ond9af", 700,68,"10.9.2023-00:00", "https://s3.amazonaws.com/thumbnails.venngage.com/template/5456834b-ba95-41a9-85b2-4abd4d313c11.png");
        Discount voucher2 = new Discount("ahk3ks", 500,68,"10.9.2023-00:00", "https://quangcaoytuong.vn/userfiles/users/5d85354d9c2a5a9251814a206bb09b7bdda23272629db699ed2cf6918f11b3a6/voucher-1-quang-cao-y-tuong.jpeg");
        Discount voucher3 = new Discount("a5d7cd", 300,68,"10.9.2023-00:00", "https://inbacha.com/wp-content/uploads/2021/05/Voucher-Coupon-Giftcard2.jpg");
        Discount voucher4 = new Discount("a3gdb6", 450,68,"10.9.2023-00:00", "https://inaniprint.com/wp-content/uploads/2021/09/in-voucher-phieu-giam-gia-inaniprint.com_.jpg");
        Discount voucher5 = new Discount("ga4fd2", 600,68,"10.9.2023-00:00", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMPh-VKqaKVVN2zlQscLIiaGrwJ00QhhooBg&usqp=CAU");
        Discount voucher6 = new Discount("fa0da8", 200,68,"10.9.2023-00:00", "https://cdn-kvweb.kiotviet.vn/kiotviet-website/wp-content/uploads/2023/06/Voucher-spa-7.png");
        Discount voucher7 = new Discount("53fagv", 400,68,"10.9.2023-00:00", "https://promacprinting.com/wp-content/uploads/2023/02/in-voucher.png");
        Discount voucher8 = new Discount("ad53fs", 100,68,"10.9.2023-00:00", "https://innhanhhaiduong.vn/uploads/shops/2021_01/in-voucher.jpg");

        // Save the voucher to Firebase
        VoucherHelper voucherHelper = new VoucherHelper();
        voucherHelper.saveVoucher(voucher1);
        voucherHelper.saveVoucher(voucher2);
        voucherHelper.saveVoucher(voucher3);
        voucherHelper.saveVoucher(voucher4);
        voucherHelper.saveVoucher(voucher5);
        voucherHelper.saveVoucher(voucher6);
        voucherHelper.saveVoucher(voucher7);
        voucherHelper.saveVoucher(voucher8);

    }
}
