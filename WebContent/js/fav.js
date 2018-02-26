var cardBook = function (dom, cpacks) {
    this.dom = $("<div class='fav_book'/>").appendTo(dom);
    this.bar = $("<div class='fav_bar'/>").appendTo(this.dom);
    this.cardpanel = $("<div class='fav_cards'/>").appendTo(this.dom);
    this.pagebar = $("<div class='fav_pagebar' />").appendTo(this.dom);
    this.currentCards = [];
    this.init();

    var me = this;
    me.cardbuild = false;
    me.cpacks = cpacks;
    me.cpacks.book = this;
    cpacks.cardBuildChanged = function (on, job) {
        me.cardbuild = on;

        if (on) {
            //me.job = job;
            me.bar.find(".fav_jobbar span").each(function () {
                $(this).removeClass("c");
                var t = $(this).text();
                if (t == job) {
                    $(this).addClass("c").click();
                    return;
                }
                if (t != "中立")
                    $(this).hide();
            })
        }
        else {
            me.bar.find(".fav_jobbar span").show();
            me.showPage();
        }
        
    };
};
cardBook.prototype.init = function (cards, selfcards) {
    this.getCards();

    var me = this;
    this.pagebar.click(function (e) {
        var pstr = $(e.target).attr("p");
        if (!pstr || pstr == "")
            return;
        var p = parseInt(pstr);
        me.showSelfPage(p);
    });
};
cardBook.prototype.render = function () {
    var me = this;
    var jobbar = $("<div class='fav_jobbar'/>").appendTo(this.bar);
    var jobs = ["圣骑士", "潜行者", "萨满", "猎人", "德鲁伊", "法师", "术士", "战士", "牧师", "中立"];
    this.job = "圣骑士";
    $.each(jobs, function (i, d) {
        $("<span class='" + (d == me.job ? "c" : "") + "'>" + d + "</span>").appendTo(jobbar);
    });
    jobbar.click(function (e) {
        if (e.target.tagName != "SPAN")
            return;
        var d = $(e.target);
        me.job = d.text();
        d.parent().find("span").removeClass("c");
        d.addClass("c");
        me.showSelfPage();
    });

    var costbar = $("<div class='fav_costbar'/>").appendTo(this.bar);
    var jobs = ["0-10", "0", "1", "2", "3", "4", "5", "6", "7+"];
    this.cost = "0-10";
    $.each(jobs, function (i, d) {
        $("<span  class='"+(d==me.cost?"c":"")+"'>" + d + "</span>").appendTo(costbar);
    });
    costbar.click(function (e) {
        if (e.target.tagName != "SPAN")
            return;
        var d = $(e.target);
        me.cost = d.text();
        d.parent().find("span").removeClass("c");
        d.addClass("c");
        me.showSelfPage();
    });


    this.currentCards = [];
    $.each(this.cards, function (i, card) {
        //if (card.group == "基础")
            me.currentCards.push(card);
    });
    $.each(this.selfcards, function (i, name) {
        for (var i = 0; i < me.currentCards.length; i++) {
            if (me.currentCards[i].name == name)
                return;
        }
        for (var i = 0; i < me.cards.length; i++) {
            if (me.cards[i].name == name) {
                me.currentCards.push(me.cards[i]);
                return;
            }
        }
    });

    this.showSelfPage(0);
};
cardBook.prototype.showSelfPage = function (page) {
    page = page || 0;
    if (!this.spage) {
        this.spage = {};
        this.spage.cpage = page;
        this.spage.pagesize = 8;
        this.spage.list = this.currentCards;
        this.spage.maxpage = this.currentCards.length / this.spage.pagesize;
        if (this.currentCards.length > 0 && this.currentCards.length % this.spage.pagesize == 0)
            this.spage.maxpage--;
    }
    if (this.job != this.spage.job || this.cost != this.spage.cost) {
        this.spage.job = this.job;
        this.spage.cost = this.cost;

        var list = [];
        var me = this;
        $.each(this.currentCards, function (i, d) {
            if (d.job != me.job)
                return;
            if (me.cost == "7+") {
                if (d.cost < 7)
                    return;
            }
            else if (me.cost != "0-10") {
                if (me.cost != d.cost + "")
                    return;
            }

            list.push(d);
        });
        this.spage.list = list;
        this.spage.maxpage = this.spage.list.length / this.spage.pagesize;
        if (this.spage.list.length > 0 && this.spage.list.length % this.spage.pagesize == 0)
            this.spage.maxpage--;
    };


    if (page > this.spage.maxpage)
        page = this.spage.maxpage;

    this.spage.cpage = page;
    this.showPage();
}
cardBook.prototype.showPage = function () {
    var me = this;
    var page = this.spage;
    var begin = page.cpage * page.pagesize;
    var end = begin + page.pagesize;
    if (end > page.list.length)
        end = page.list.length;
    this.cardpanel.empty();
    for (var i = begin; i < end; i++) {
        var c = page.list[i];
        var item = $("<div class='card_item' cname='"+c.name+"'/>").appendTo(this.cardpanel);
        
//        var job="";
//        if(c.job=='中立')
//        	job="neutral";
//        else if(c.job=='德鲁伊')
//        	job="druid";
//        else if(c.job=='法师')
//        	job="mage";
//        else if(c.job=='潜行者')
//        	job="rogue";
//        else if(c.job=='萨满')
//        	job="shaman";
//        else if(c.job=='圣骑士')
//        	job="paladin";
//        else if(c.job=='猎人')
//        	job="hunter";
//        else if(c.job=='牧师')
//        	job="priest";
//        else if(c.job=='术士')
//        	job="warlock";
//        else if(c.job=='战士')
//        	job="warrior";
        
        var img = $("<img src='http://hearthstone.nos.netease.com/1/cards/" 
        		+ c.imageid + "'/>").appendTo(item);
        //if (c.type == "Minion") {
            //img.addClass("bigcard");
        //}
        
        
        var cdiv = $("<div class='cdiv'/>").appendTo(item);
        /*
        $("<h1>" + c.name + "</h1>").appendTo(cdiv);
        $("<div class='cost'>" + c.cost + "</div>").appendTo(cdiv);
        if (c.attack > 0)
            $("<div class='attack'>" + c.attack + "</div>").appendTo(cdiv);
        if (c.blood > 0)
            $("<div class='blood'>" + c.blood + "</div>").appendTo(cdiv);
        if (c.race != "")
            $("<div class='race'>" + c.race + "</div>").appendTo(cdiv);
		
        var rate = 'L1';
        if (c.rate == "免费")
            rate = "L2";
        else if (c.rate == "稀有")
            rate = "L3";
        else if (c.rate == "史诗")
            rate = "L4";
        else if (c.rate == "传说")
            rate = "L5";
        cdiv.addClass(rate);
        $("<div class='rate'>" + c.rate + "</div>").appendTo(cdiv);
        $("<div class='effects'>" + c.effects.join("<br/>") + "</div>").appendTo(cdiv);

        item.mouseenter(function () {
            $(this).find("img").hide();
            $(this).find(".cdiv").show();
        })
        .mouseleave(function () {
            $(this).find("img").show();
            $(this).find(".cdiv").hide();
        })
        */
        if (this.cardbuild) {
//            $("<div class='btn' cname='" + c.name + "'>选取</div>").appendTo(cdiv).click(function () {
//                me.cpacks.addCard($(this).attr("cname"));
//            });
        	item.click(function () {
        		me.cpacks.addCard($(this).attr("cname"));
        	});
        }
		
    }

    this.pagebar.empty();
    var currentpage = page.cpage;
    var maxpage = page.maxpage;
    if (currentpage > 0)
        $("<div class='prev'  p='" + (currentpage - 1) + "'>上一页</div>").appendTo(this.pagebar);
    var pcontent = $("<div class='pcontent'></div>").appendTo(this.pagebar);

    var pbegin = Math.max(0, currentpage - 5);
    var pend = Math.min(maxpage, currentpage + 5);

    for (var i = pbegin; i <= pend; i++) {
        $("<div class='" + (i == currentpage ? "c" : "") + "'  p='" + i + "'>" + (i + 1) + "</div>").appendTo(pcontent);
    }

    if (currentpage < maxpage - 1)
        $("<div class='next' p='" + (currentpage + 1) + "'>下一页</div>").appendTo(this.pagebar);
};
cardBook.prototype.getCards = function () {
    var me = this;
    server("legend.favorites.getCards", function (d) {
        me.cards=d.cards;
        me.selfcards=d.selfcards;
        me.render();
    });
};


var cardPacks = function (dom) {
    this.title = $("<h1>自定义套牌：</h1>").appendTo(dom);
    this.packspanel = $("<div class='packspanel'/>").appendTo(dom);
    this.packbuilder = $("<div class='packbuilder' />").hide().appendTo(dom);
    this.init();
    this.cardBuildChanged = function (d) { };
};

cardPacks.prototype.init = function () {
    this.getPacks();
};

cardPacks.prototype.render = function () {
    var me = this;
    this.packspanel.empty();
    for (var i = 0; i < 9; i++) {
        if (i < this.packs.length) {
            var pack = this.packs[i];
            var packpanel = $("<div class='packpanel'/>").appendTo(this.packspanel);
            $("<h1>" + pack.name + "</h1>").appendTo(packpanel).click(function () {
                var n = $(this).text();
                for (var i = 0; i < me.packs.length; i++) {
                    if (me.packs[i].name == n) {
                        me.beginBuilder(me.packs[i]);
                        break;
                    }
                }
            });
            $("<span>职业：" + pack.job + "</span>").appendTo(packpanel);
            $("<font title='删除套牌'>x</font>").appendTo(packpanel).click(function () {
                me.deletePack($(this).parent().find("h1").text());
            });

        }
        else if (i == this.packs.length) {
            var addpanel = $("<div class='addpanel'/>").appendTo(this.packspanel);
            $("<input/>").appendTo(addpanel);
            var select = $("<select></select>").appendTo(addpanel);
            $.each(["圣骑士", "潜行者", "萨满", "猎人", "德鲁伊", "法师", "术士", "战士", "牧师"], function (i, d) {
                $("<option>" + d + "</option>").appendTo(select);
            });
            $("<div class='btn'>新增套牌</div>").appendTo(addpanel).click(function () {
                var name = $.trim($(this).parent().find("input").val());
                var job = $(this).parent().find("select").val();
                me.newPack(name, job);
            });
        }
    };

    this.renderdust();
};
cardPacks.prototype.renderdust = function () {
    if ($("#head_dust").length == 0) {
        $("<div title='奥术之尘' class='dust' id='head_dust'>" + this.dust + "</div>").insertAfter($("#head_money"));
    }
    else {
        $("#head_dust").text(this.dust);
    }
};
cardPacks.prototype.getPacks = function () {
    var me = this;
    server("legend.favorites.getPacks", function (d) {
        me.packs = d.packs;
        me.dust = d.dust;
        me.render();
    });
};
cardPacks.prototype.newPack = function (name, job) {
    if (name == "") {
        r.error("新套牌名称不能为空！")
        return;
    }
    if(name.length>10){
        r.error("新套牌名称太长了！")
        return;
    }
    for (var i = 0; i < this.packs.length; i++) {
        if (this.packs[i].name == name) {
            r.error("新套牌名称已存在！")
            return;
        }
    }

    var me = this;
    server("legend.favorites.newPack", name, job, function () {
        me.packs.push({ name: name, job: job, cards: [] });
        me.render();
    });
};
cardPacks.prototype.deletePack = function (name) {
    if (!confirm("你确定要删除套牌"+name+"吗？"))
        return;

    var me = this;
    server("legend.favorites.deletePack", name, function () {
        for (var i = 0; i < me.packs.length; i++) {
            if (me.packs[i].name == name) {
                me.packs.splice(i, 1);
                break;
            }
        }
        me.render();
    });
};
cardPacks.prototype.beginBuilder = function (pack) {
    var me = this;
    this.cardBuildChanged(true,pack.job);
    this.buildingPack = pack;
    this.packbuilder.empty().show();
    this.packspanel.hide();
    $("<h1>" + pack.name + "</h1>").appendTo(this.packbuilder);
    $("<font title='退出选牌'>x</font>").appendTo(this.packbuilder).click(function(){
        me.cardBuildChanged(false);
        me.packbuilder.hide();
        me.packspanel.show();
    });
    this.cardnumber = $("<div class='cardnumber'></div>").appendTo(this.packbuilder)
    this.cardfinish = $("<div class='btn'>完成</div>").appendTo(this.packbuilder).click(function () {
        me.finishBuilder();
    });
    this.cardpanel = $("<div class='cardpanel' />").appendTo(this.packbuilder).click(function (e) {
        if (e.target.tagName != "SPAN")
            return;
        var cname = $(e.target).parent().find("span").text();
        var cards = me.buildingPack.cards;
        for (var i = 0; i < cards.length; i++) {
            if (cards[i].name == cname) {
                cards.splice(i, 1);
                break;
            }
        }
        me.renderCards();
    });

    this.renderCards();
};
cardPacks.prototype.renderCards = function () {
    var me = this;
    if (!this.buildingPack)
        return;
    this.cardpanel.empty();
    var cs = this.buildingPack.cards;
    var cards = [];
    var cardb = this.book.cards;
    $.each(cs, function (ief, d) {
        for (var i = 0; i < cardb.length; i++) {
            if (cardb[i].name == d) {
                cards.push(cardb[i]);
            }
        }
    });

    this.cardnumber.text(cards.length + "/30");

    var dic = {};
    for (var i = 0; i < cards.length; i++) {
        var c = cards[i];
        if (!dic[c.cost])
            dic[c.cost] = [];
        var list = dic[c.cost];
        var finded = false;
        for (var j = 0; j < list.length; j++) {
            if (list[j].name == c.name) {
                list[j].count++;
                finded = true;
                break;
            }
        }
        if (!finded) {
            list.push({ name: c.name, count: 1 });
        }
    }

    for (var i = 0; i <= 10; i++) {
        var list = dic[i];
        if (!list)
            continue;
        for (var j = 0; j < list.length; j++) {
            var c = list[j];
            var cd = $("<div><i>" + i + "</i><span>" + c.name + "</span></div>").appendTo(this.cardpanel);
            if (c.count > 1)
                $("<b>" + c.count + "</b>").appendTo(cd);
            $("<font cname='" + c.name + "'>x</font>").appendTo(cd).click(function () {
                me.removeCard($(this).attr("cname"));
            });
        }
    }
};
cardPacks.prototype.finishBuilder = function () {
    if (!this.buildingPack)
        return;
    var cards = this.buildingPack.cards;
    if (cards.length < 30) {
        r.error("套牌不足30张！");
        return;
    }
    this.cardBuildChanged(false);

    //todo
    var pack = this.buildingPack;
    var me = this;
    server("legend.favorites.storePack", pack.name, pack.job, pack.cards.join("__"), function () {
        me.packbuilder.hide();
        me.packspanel.show();
    });
};
cardPacks.prototype.addCard = function (card) {
    if (!this.buildingPack)
        return;
    var cards = this.buildingPack.cards;
    if (cards.length >= 30) {
        r.error("套牌超过30张！");
        return;
    }
    if (this.isMax(card)) {
        r.error("一副套牌不能拥有更多"+card+"！");
        return;
    }

    this.buildingPack.cards.push(card);
    this.renderCards();
};
cardPacks.prototype.removeCard = function (card) {
    if (!this.buildingPack)
        return;
    var cards = this.buildingPack.cards;

    for (var i = 0; i < cards.length; i++) {
        if (cards[i] == card) {
            cards.splice(i, 1);
            break;
        }
    }
    this.renderCards();
};
cardPacks.prototype.isMax = function (cname) {
    if (!this.buildingPack)
        return false;
    var count = 0;
    $.each(this.buildingPack.cards, function (i, c) {
        if (c == cname)
            count++;
    });

    var max = 2;
    var cardb = this.book.cards;
    $.each(cardb, function (ief, d) {
        if (cname == d.name)
            max = (d.rate == "传说" ? 1 : 2);
    });

    if (count >= max)
        return true;
    return false;
};

var favEngine = function (dom) {
    this.dom = dom;
    this.left = $("<div class='fav_left'/>").appendTo(this.dom);
    this.right = $("<div class='fav_right'/>").appendTo(this.dom);

    this.packs = new cardPacks(this.right);
    this.book = new cardBook(this.left,this.packs);

};

favEngine.prototype.render = function () {

};

