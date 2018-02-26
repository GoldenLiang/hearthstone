var movies = {};
movies.timeStep = 20;
movies.showTitle = function (dom,classname,text,cb) {
    $("<div class=' box "+classname+"'/>").text(text).appendTo(dom)
    .delay(40 * movies.timeStep).hide("fade", {}, 10 * movies.timeStep, function () {
        $(this).remove();
        cb();
    });
};

var getCard = function (name, callback) {
    if (!getCard.cards)
        getCard.cards = {};
    if (getCard.cards[name]) {
        callback(getCard.cards[name]);
        return;
    }

    server("legend.client.getCard", name, function (d) {
        getCard.cards[name] = d;
        callback(d);
    });
};
var serverAttack = function (aid, did, callback) {
    server("legend.client.attack", aid, did, function (d) {
        callback(d);
    });
};
var TargetPanel = function () {
    this.dom = $("<div class='targetpanel'/>").hide().appendTo($("body"));
    this.over = $("<div class='over'/>").appendTo(this.dom);
    this.panel = $("<div class='panel'/>").appendTo(this.dom);
    this.title = $("<div class='tptitle'/>").appendTo(this.dom);

    var me = this;
    this.over.click(function () {
        me.dom.hide();
        if (me.cancel)
            me.cancel();
    });
    this.panel.click(function (e) {
        var dom = $(e.target);
        if (dom.attr("class") != "item")
            return;

        var id = dom.attr("tid");
        me.dom.hide();
        if (me.selected)
            me.selected(id);
    });
};
TargetPanel.prototype.show = function (title, doms, selected, cancel) {
    this.title.text(title);
    this.selected = selected;
    this.cancel = cancel;
    this.panel.empty();
    var me = this;
    
    
    
    $(doms).each(function () {
        var item = $(this);
        var pos = item.offset();
        var width = item.outerWidth();
        var height = item.outerHeight();
        var tid = item.attr("id");
        $("<div class='item'/>").attr("tid", tid).css({
            left: pos.left, top: pos.top, width: width, height: height
        }).appendTo(me.panel);
    });
    this.dom.show();
};
var targetPanel = new TargetPanel();

var CardHeap = function (dom) {
    this.dom = $("<div class='cardheap' title='剩余牌数'/>").appendTo(dom);
};
CardHeap.prototype.render = function (count) {
    this.count = count;
    this.dom.text(count);
};

var PowerPanel = function (dom) {
    this.dom = $("<div class='powerpanel'/>").appendTo(dom);
};
PowerPanel.prototype.render = function (power, maxpower) {
    this.dom.empty();
    $("<font></font>").text(power + "/" + maxpower).appendTo(this.dom);
    for (var i = 0; i < 10; i++) {
        var span = $("<span ></span>").appendTo(this.dom);
        if (i < power)
            span.addClass("active");
        else if (i < maxpower)
            span.addClass("lost");
    }
};


var Card = function (dom, pool) {
    this.pool = pool;
    this.dom = $("<div class='card'/>").appendTo(dom);
   
    this.d = {};
    this.d2=null;
};
Card.prototype.render = function (d) {
    this.id = d.id;
    this.d=d;
    
    this.dom.attr("id", this.id);

    if (!d.cardname || d.cardname == "")
        return;
    this.name = d.cardname;
    var me = this;
    getCard(me.name, function (c) {
        me.d2 = c;
        
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
        
        $("<img src='http://hearthstone.nos.netease.com/1/cards/" + c.imageid +"'/>").appendTo(me.dom);
        var p = $("<div/>").addClass('detail').hide().appendTo(me.dom);
        $("<h1>" + c.name + "</h1>").appendTo(p);
        $("<span class='cost'>" + c.cost + "</span>").appendTo(p);
        $("<p>" + c.effects + "</p>").appendTo(p);
        if (c.attack > 0 || c.blood > 0) {
            $("<span class='attack'>" + c.attack + "</span>").appendTo(p);
            $("<span class='blood'>" + c.blood + "</span>").appendTo(p);
        }
        if (c.type == "Minion")
            me.dom.addClass("minioncard");
    });

    this.dom.mouseenter(function () {
        $(this).find("img").hide();
        $(this).find(".detail").show();
    });
    this.dom.mouseleave(function () {
        $(this).find("img").show();
        $(this).find(".detail").hide();
    });

    //alert(me.d.usable);
    
    if (me.d.usable) {
        setTimeout(function () {
            r.noSelect(me.dom);
            me.dom.draggable({ revert: "invalid" });
        });
    }
};
Card.prototype.cancel = function () {
    var me = this;
    setTimeout(function () {
        me.dom.css({ left: 0, top: 0 });
    });
};
Card.prototype.use = function (index) {
    if (!this.d || !this.pool.field.d.turn) {
        this.cancel();
        return;
    }

    var me = this;
    if (this.d.needTarget) {
    	
    	var doms=[];
    	for(var i=0;i<this.d.targets.length;i++)
    	{
    		var mh=this.pool.field.action.findmh(this.d.targets[i]);
    		
    		if(mh.id=="g61" || mh.id=="g62")
    			doms.push(mh.face);
    		else	
    			doms.push(mh.dom);
    	}
    	
        targetPanel.show("请选择一个目标", doms, function (tid) {
            //alert(tid);
            me.serverUse(tid,index);
        }, function () {
            me.cancel();
        });
        return;
    }
    else {
        me.serverUse("",index);
    }
    
    me.dom.hide();
};
Card.prototype.serverUse = function (target,index) {
    var me = this;
    var field = this.pool.field;
    var power = field.d.player.power;
    if (this.d.cost > power) {
        r.error("魔法不足！");
        me.cancel();
        return;
    }
    
    
    server("legend.client.userCard", this.id, target, index, function () {
        //me.pool.remove(me.id);
        //field.sync();
    });


};


Card.prototype.addOuterState = function (sid,key,value) {
	
    if(!this.d.outerstate[sid])
    {
    	this.d.outerstate[sid]={};
    	this.d.outerstate[sid][key]=value;
    }
    else
    {
    	this.d.outerstate[sid][key]=value;
	}
        
};

Card.prototype.removeOuterState = function (sid) {
	delete this.d.outerstate[sid];
};

Card.prototype.targets = function (arr) {
    this.d.targets=Array();
    for(var i=0;i<arr.length;i++)
    {
    	this.d.targets.push(arr[i]);
    }
    
};

Card.prototype.usable = function (value) {
    this.d.usable=value;
    
};


var CardPool = function (dom, self, field) {
    this.field = field;
    this.self = self;
    this.dom = $("<div class='cardpool'/>").appendTo(dom);
    this.cs = [];
    this.cardWidth = 105;


};
CardPool.prototype.render = function (cs) {
    //alert("callpool render");
	
	var list = [];
    for (var i = 0; i < cs.length; i++) {
        var name = cs[i].cardname||"";

        var findcard = false;
        for (var j = 0; j < this.cs.length; j++) {
            var card = this.cs[j];
            if (card.name == name) {
                list.push(card);
                findcard = true;
                this.cs.splice(j, 1);
                break;
            }
        }
        
        
        if (!findcard) {
            var c = new Card(this.dom, this);
            c.render(cs[i]);
            list.push(c);
        }
    }

    var tempdiv = $("<div/>").append(this.dom.children());
    for (var i = 0; i < list.length; i++)
        this.dom.append(list[i].dom);
    tempdiv.empty();

    this.cs = list;

    this.refreshWidth();

}

CardPool.prototype.findCard = function (cid) {
    
	var i=0;
	for (i = 0; i < this.cs.length; i++) {
        if (this.cs[i].id == cid) {
            
            break;
        }
    }
    
    return this.cs[i];
};

CardPool.prototype.remove = function (cid) {
    for (var i = 0; i < this.cs.length; i++) {
        if (this.cs[i].id == cid) {
            this.cs[i].dom.remove();
            this.cs.splice(i, 1);
            break;
        }
    }
    this.refreshWidth();

};
CardPool.prototype.use = function (cid, index) {
    for (var i = 0; i < this.cs.length; i++) {
        if (this.cs[i].id == cid) {
            this.cs[i].use(index);
            break;
        }
    }
};
CardPool.prototype.refreshWidth = function () {
    var width = 105;
    var length=1;
    
    if(this.cs.length>1)
    	length=this.cs.length;
    
    this.dom.css('width', width * length);
    this.dom.css('margin-left', 0 - width * length / 2);
};
CardPool.prototype.add = function (gamecard, cb) {
    var c = new Card(this.dom, this);
    c.render(gamecard);
    this.cs.push(c);
    this.refreshWidth();
    cb();
};
CardPool.prototype.lost = function (cid, cb) {
    this.remove(cid);
    cb();
};

var Minion = function (dom, self, pool, field) {
    this.pool = pool;
    this.dom = $("<div class='minion' />").appendTo(dom);
    this.self = self;
    this.firstrender = true;
    
    this.field=field;
};
Minion.prototype.render = function (d) {
    this.d = d;
    this.dom.attr("id", d.id);
    this.id = d.id;

    this.dom.empty();
    //this.dom.attr("title", d.name);
    $("<img src='http://hearthstone.nos.netease.com/1/cards/" + d.imageid + "'/>").appendTo(this.dom);
    //$("<h1>" + d.name + "</h1>").appendTo(this.dom);
    $("<span class='attack'>" + d.attack + "</span>").appendTo(this.dom);
    $("<span class='blood'>" + d.blood + "</span>").appendTo(this.dom);
    var div = $("<div class='state'/>").appendTo(this.dom);
    var me = this;
    me.dom.css("overflow", "hidden");
    this.dom.mousedown(function () {
        me.dom.css("overflow", "");
    });
    this.dom.mouseleave(function () {
        me.dom.css("overflow", "hidden");
    });

    if (this.self) {
        this.dom.addClass("attackhelper");
        r.noSelect(this.dom);
        this.dom.draggable({
            cursor: "default",
            cursorAt: { top: 0, left: 0 },
            helper: function (event) {
                return $("<div class='attack-helper'></div>");
            }
        });
    }
    else if (this.firstrender) {
        this.dom.droppable({
            accept: ".attackhelper",
            activeClass: "minion-hover",
            hoverClass: "minion-active",
            drop: function (event, ui) {
                var id = ui.draggable.attr("id");
                me.beAttack(id);
            }
        });
    }
    this.firstrender = false;

    this.renderState();

};
Minion.prototype.renderState = function () {
    var div = this.dom.find(".state");
    div.empty();
    var ready = false;
    
    var attackable=this.d.innerstate["attackable"];
    var attackcount=this.d.innerstate["attackcount"];
    var action=this.d.innerstate["action"];
    var freeze=this.d.innerstate["freeze"];
    
    var states=Array();
    
    
    if(attackable=="true" && attackcount>0 && action=="ready" && !freeze)
    {
    	ready=true;
    }
    else
    {
    	if(freeze)
    		states[states.length]="冰冻";
    	else
    		states[states.length]="休息";
    	
    	ready=false;
    }
    
    for(var p in this.d.innerstate)
	{
    	if(p=="taunt")
		{
    		states[states.length]="嘲讽";
		}
    	else if(p=="shield")
		{
    		states[states.length]="圣盾";
		}
	}
    
//    for (var i = 0; i < this.d.innerstate.length; i++) {
//        var s = this.d.states[i];
//        if (s == "休息")
//            ready = false;
//        if (s.length > 2) {
//            $("<font title='" + s + "'>" + s.substr(0, 2) + "</font>").appendTo(div);
//        }
//        else {
//            $("<font >" + s + "</font>").appendTo(div);
//        }
//    }
    
	  for (var i = 0; i < states.length; i++) {
		  var s = states[i];
		  
		  if (s.length > 2) {
		      $("<font title='" + s + "'>" + s.substr(0, 2) + "</font>").appendTo(div);
		  }
		  else {
		      $("<font >" + s + "</font>").appendTo(div);
		  }
	  }
    
    if (this.self) {
        if (ready && this.field.d.turn)
            this.dom.draggable("option","cancel","");
        else 
            this.dom.draggable("option","cancel","#"+this.dom.attr("id"));
    }
};
Minion.prototype.beAttack = function (id) {
    //alert(id + " attack " + this.d.id);

	// this.id should be inside id's targets
	var attackMinion=this.field.action.findmh(id);
			
	var targets=attackMinion.d.targets;
	//alert(targets);
	var found=false;
	for(var i=0;i<targets.length;i++)
	{
		if(this.id==targets[i])
			found=true;
	}
	
	if(!found)
	{
		alert("不能攻击该目标");
	}
	
	if(found)
	{
	    serverAttack(id, this.d.id, function (d) {
	        if (d != true)
	            r.error(d);
	        //window.field.sync();
	    });
	}
};

//Minion.prototype.hurt = function (count, cb) {
//    var me = this;
//    movies.showTitle(this.dom, "hurt", 0 - count, function () {
//        me.d.blood -= count;
//        me.dom.find(".blood").text(me.d.blood);
//        cb();
//    });
//};
//
//Minion.prototype.cure = function (count, cb) {
//    var me = this;
//    movies.showTitle(this.dom, "cure", count, function () {
//        me.d.blood += count;
//        me.dom.find(".blood").text(me.d.blood);
//        cb();
//    });
//};

Minion.prototype.changeAttack = function (count) {
    var t = count - this.d.attack;
    var buff = t > 0;
    this.d.attack = count;
    var me = this;
    movies.showTitle(this.dom, buff ? "buff" : "debuff", t, function () {
        me.dom.find(".attack").text(count);
    });
};

Minion.prototype.blood = function (count, cb) {
    var me = this;
    
    var title="";
    if(count<me.d.blood)
    	title="hurt";
    else
    	title="cure";
    
    movies.showTitle(this.dom, title, count-me.d.blood, function () {
        me.d.blood = count;
        me.dom.find(".blood").text(me.d.blood);
        cb();
    });
};

Minion.prototype.index = function (count, cb) {
    var me = this;
    
    me.d.index = count;
    this.pool.refresh();
};

Minion.prototype.maxblood = function (count, cb) {
    var me = this;
    
    me.d.maxblood = count;
};

Minion.prototype.die = function (cb) {
    var me = this;
    this.dom.effect("shake", {}, 20 * movies.timeStep, function () {
        me.pool.remove(me.id);
        cb();
    });
};

//Minion.prototype.addStates = function (states) {
//    var ss = this.d.states;
//    for (var i = 0; i < states.length; i++) {
//        var s = states[i];
//        var find = false;
//        for (var j = 0; j < ss.length; j++) {
//            if (ss[i] == s) {
//                find = true;
//                break;
//            }
//        }
//        if (!find)
//            ss.push(s);
//    }
//    this.renderState();
//};

Minion.prototype.addInnerState = function (key, value) {
    this.d.innerstate[key]=value;
    
    this.renderState();
};

//Minion.prototype.lostStates = function (states) {
//    var ss = this.d.states;
//    for (var i = ss.length - 1; i >= 0; i--) {
//        for (var j = 0; j < states.length; j++) {
//            if (states[j] == ss[i]) {
//                ss.splice(i, 1);
//                break;
//            }
//        }
//    }
//    this.renderState();
//};

Minion.prototype.removeInnerState = function (key) {
	delete this.d.innerstate[key];
    
    this.renderState();
};

Minion.prototype.addOuterState = function (sid, key, value) {
    if(!this.d.outerstate[sid])
    {
    	this.d.outerstate[sid]={};
    	this.d.outerstate[sid][key]=value;
    }
    else
    {
    	this.d.outerstate[sid][key]=value;
	}
    
    this.renderState();
};

Minion.prototype.removeOuterState = function (sid) {
    delete this.d.outerstate[sid];
    
    this.renderState();
};

Minion.prototype.targets = function (arr) {
    this.d.targets=Array();
    for(var i=0;i<arr.length;i++)
    {
    	this.d.targets.push(arr[i]);
    }
    
};

var MinionPool = function (dom, self, field) {
    this.dom = $("<div class='minionpool'/>").appendTo(dom);
    this.minions = [];
    this.self = self;
    
    this.field=field;
};
MinionPool.prototype.render = function (ms) {

    var list = [];
    for (var i = 0; i < ms.length; i++) {
        var id = ms[i].id;

        var findcard = false;
        for (var j = 0; j < this.minions.length; j++) {
            var m = this.minions[j];
            if (m.id == id) {
                m.render(ms[i]);
                list.push(m);
                findcard = true;
                this.minions.splice(j, 1);
                break;
            }
        }
        if (!findcard) {
            var c = new Minion(this.dom, this.self, this, this.field);
            c.render(ms[i]);
            list.push(c);
        }
    }

    var tempdiv = $("<div/>").append(this.dom.children());
    for (var i = 0; i < list.length; i++)
        this.dom.append(list[i].dom);
    tempdiv.empty();

    this.minions = list;

    this.refreshWidth();


};

MinionPool.prototype.refresh = function () {
    
	// adjust the minions according to their index
	
	// first, sort the minions
	function sortByIndex(a, b){
	  var aIndex = a.d.index;
	  var bIndex = b.d.index; 
	  return ((aIndex < bIndex) ? -1 : ((aIndex > bIndex) ? 1 : 0));
	}

	this.minions.sort(sortByIndex);
	
	// append the element again
	for (var i = 0; i < this.minions.length; i++)
        this.dom.append(this.minions[i].dom);
	
	this.refreshWidth();
};

MinionPool.prototype.refreshWidth = function () {
    var width = 105;
    var length=1;
    
    if(this.minions.length>1)
    	length=this.minions.length;
    
    this.dom.css('width', width * length);
    this.dom.css('margin-left', 0 - width * length / 2);
};
MinionPool.prototype.add = function (m) {
    var c = new Minion(this.dom, this.self, this, this.field);
    c.render(m);
    this.minions.push(c);
    this.refresh();
};
MinionPool.prototype.remove=function(id){
    for(var i=0;i<this.minions.length;i++){
        if(this.minions[i].id==id){
            this.minions[i].dom.remove();
            this.minions.splice(i,1);
            break;
        }
    }
    this.refreshWidth();
};

var Weapon=function (dom, self, field)
{
	this.dom=$("<div class='weapon'/>").appendTo(dom).css("visibility", "hidden");
	this.self=self;
	this.field=field;
	
};

Weapon.prototype.render=function(d)
{
	this.d=d;
	this.id=d.id;
		
	this.dom.attr("title", this.d.name).empty().css("visibility", "visible");
    $("<span class='attack'>" + this.d.attack + "</span>").appendTo(this.dom);
    $("<span class='blood'>" + this.d.blood + "</span>").appendTo(this.dom);
   
};

Weapon.prototype.renderState=function()
{
	
};

Weapon.prototype.addinnerstate=function(key, value)
{
	this.d.innerstate[key]=value;
	this.renderState();
};

Weapon.prototype.removeinnerstate=function(key)
{
	delete this.d.innerstate[key];
	this.renderState();
};

Weapon.prototype.addouterstate=function(sid, key, value)
{
	if(!this.d.outerstate[sid])
	{
		this.d.outerstate[sid]={};
	}
	
	this.d.outerstate[sid][key]=value;
	
	this.renderState();
};

Weapon.prototype.removeouterstate=function(sid)
{
	delete this.d.outerstate[sid];
	this.renderState();
};

var HeroPanel = function (dom, self, field) {
    this.dom = $("<div class='heropanel'/>").appendTo(dom);
    //this.weapon = $("<div class='weapon'/>").appendTo(this.dom).css("visibility", "hidden");
    this.weapon=new Weapon(this.dom, self, field);
    this.face = $("<div class='face'/>").appendTo(this.dom);
    this.skill = $("<div class='skill'/>").appendTo(this.dom);
    this.self = self;
    this.firstrender = true;

    this.field = field;
};
HeroPanel.prototype.render = function (d) {
    this.attack = d.attack;
    this.id = d.id;
    this.d = d;
    this.d.innerstate = [];
    //weapon
    if (d.weapon) {
//        this.weapon.dom.attr("title", d.weapon.name).empty().css("visibility", "visible");
//        $("<span class='attack'>" + d.weapon.attack + "</span>").appendTo(this.weapon.dom);
//        $("<span class='blood'>" + d.weapon.blood + "</span>").appendTo(this.weapon.dom);
    	
    	this.weapon.render(d.weapon);
    	
    	if(d.weapon.blood<=0)
        	this.removeweapon();
    };
    
    

    //face
    this.face.empty();
    this.face.attr("id", d.id);
    $("<h1 class='name'>" + d.name + "</h1>").appendTo(this.face);
    $("<div class='state'></div>").appendTo(this.face);
    $("<span class='attack'>" + d.attack + "</span>").appendTo(this.face);
    $("<span class='guard'>" + d.guard + "</span>").appendTo(this.face);
    $("<span class='blood'>" + d.blood + "</span>").appendTo(this.face);
    if (!d.attack || d.attack < 0)
        this.face.find(".attack").hide();
    if (d.guard <= 0)
        this.face.find(".guard").hide();

    //skill
    if (d.skill) {
        this.skill.attr("title", d.skill.remark).empty().css("visibility", "visible");
        $("<span class='cost'>" + d.skill.cost + "</span>").appendTo(this.skill);

        var skillbtn = $("<p>" + d.skill.name + "</p>").appendTo(this.skill);
        if (this.self) {
            var me = this;
            this.skill.addClass("self_skill").click(function () {
                me.useSkill();
            });
        }

    }

    if (this.firstrender) {
        this.firstrender = true;
        var me = this;
        if (this.self) {
            this.face.addClass("attackhelper");
            this.face.draggable({
                cursor: "default",
                cursorAt: { top: 0, left: 0 },
                helper: function (event) {
                    return $("<div class='attack-helper'></div>");
                }
            });
        }
        else {
            
        	this.face.droppable({
                accept: ".attackhelper",
                activeClass: "hero-hover",
                hoverClass: "hero-active",
                drop: function (event, ui) {
                    var id = ui.draggable.attr("id");
                    me.beAttack(id);
                }
            });
        }

    }
};
HeroPanel.prototype.beAttack = function (id) {
    //alert(id + " attack " + this.id); 
	
	var attackMinion=this.field.action.findmh(id);
	
	var targets=attackMinion.d.targets;
	//alert(targets);
	var found=false;
	for(var i=0;i<targets.length;i++)
	{
		if(this.id==targets[i])
			found=true;
	}
	
	if(!found)
	{
		alert("不能攻击该目标");
	}
	
	if(found)
	{
	    serverAttack(id, this.id, function (d) {
	    	if (d != true)
	            r.error(d);
	    	//window.field.sync();
	    });
	}
};
//HeroPanel.prototype.hurt = function (count, cb) {
//    var me = this;
//    movies.showTitle(this.face, "hurt", 0-count, function () {
//        me.d.blood -= count;
//        me.face.find(".blood").text(me.d.blood);
//        cb();
//    });
//};
//HeroPanel.prototype.cure = function (count, cb) {
//    var me = this;
//    movies.showTitle(this.face, "cure", count, function () {
//        me.d.blood += count;
//        me.face.find(".blood").text(me.d.blood);
//        cb();
//    });
//};

HeroPanel.prototype.blood = function (count, cb) {
	var me = this;
	
	var title="";
	if(count<me.d.blood)
		title="hurt";
	else
		title="cure";
	
	movies.showTitle(this.face, title, count-me.d.blood, function () {
	  me.d.blood = count;
	  me.face.find(".blood").text(me.d.blood);
	  cb();
	});
};

//HeroPanel.prototype.guard_hurt = function (count, cb) {
//    var me = this;
//    movies.showTitle(this.face, "guard_hurt", 0 - count, function () {
//        me.d.guard -= count;
//        var dom = me.face.find(".guard");
//        dom.text(me.d.guard);
//        if (me.d.guard <= 0)
//            dom.hide();
//
//        cb();
//    });
//};
//HeroPanel.prototype.guard_cure = function (count, cb) {
//    var me = this;
//    movies.showTitle(this.face, "guard_cure", count, function () {
//        me.d.guard += count;
//        var dom = me.face.find(".guard");
//        dom.text(me.d.guard);
//        if (me.d.guard > 0)
//            dom.show();
//        cb();
//    });
//};

HeroPanel.prototype.guard = function (count, cb) {
	var me = this;
	
	var title="";
	if(count<me.d.guard)
		title="guard_hurt";
	else 
		title="guard_cure";
		
	movies.showTitle(this.face, title, count-me.d.guard, function () {
	  me.d.guard = count;
	  var dom = me.face.find(".guard");
	  dom.text(me.d.guard);
	  if (me.d.guard > 0)
	      dom.show();
	  cb();
	});
};

HeroPanel.prototype.die = function (cb) {
    var me = this;
    this.face.effect("explode", {}, 20 * movies.timeStep, function () {
        me.face.show().css("visibility", "hidden");
        cb();
    });
};
HeroPanel.prototype.changeAttack = function (count) {
    var t = count - this.d.attack;
    var buff = t > 0;
    this.d.attack = count;
    var me = this;
    movies.showTitle(this.face, buff ? "buff" : "debuff", t, function () {
        var ad=me.face.find(".attack");
        ad.text(count);
        if (count > 0)
            ad.show();
        else
            ad.hide();
    });
    
    this.renderState();
};
//HeroPanel.prototype.addweapon = function (name, attack, blood) {
//    this.weapon.empty().css("visibility","visible");
//    this.weapon.attr("title", name);
//    $("<span class='attack'>" + attack + "</span>").appendTo(this.weapon);
//    $("<span class='blood'>" + blood + "</span>").appendTo(this.weapon);
//    this.face.find(".attack").text(attack).show();
//};
//HeroPanel.prototype.changeweapon = function (attack, blood) {
//    this.weapon.find(".attack").text(attack);
//    this.weapon.find(".blood").text(blood);
//    this.face.find(".attack").text(attack);
//};

HeroPanel.prototype.changeweaponattack = function (attack) {
    
	this.weapon.dom.find(".attack").text(attack);
    
    //this.face.find(".attack").text(attack);
    
    this.renderState();
};

HeroPanel.prototype.changeweaponblood = function (blood) {
    
    if(blood<=0)
	{
    	this.weapon.dom.find(".blood").text(blood);
    	this.removeweapon();
	}
    else
    {
    	this.weapon.dom.css("visibility","visible");
    	this.weapon.dom.find(".blood").text(blood);
    	//this.face.find(".attack").show();
    }
    
    this.renderState();
    
};

HeroPanel.prototype.addweaponinnerstate = function (key, value) {
	this.weapon.addinnerstate(key,value);
	
	this.renderState();
};

HeroPanel.prototype.removeweaponinnerstate = function (key) {
	this.weapon.removeinnerstate(key);
	
	this.renderState();
};

HeroPanel.prototype.addweaponouterstate = function (sid, key, value) {
	this.weapon.addouterstate(sid, key, value);
};

HeroPanel.prototype.removeweaponouterstate = function (sid) {
	this.weapon.removeouterstate(sid);
};

HeroPanel.prototype.removeweapon = function () {
    this.weapon.dom.css("visibility","hidden");
    this.face.find(".attack").hide();
    
    
};
HeroPanel.prototype.showHeroAttack = function (turn) {
    if (!turn) {
        this.face.find(".attack").hide();
        return;
    }

    var text = this.face.find(".attack").text();
    var count = parseInt(text);
    if(count>0)
        this.face.find(".attack").show();
};
//HeroPanel.prototype.addStates = function (states) {
//    var ss = this.d.states;
//    for (var i = 0; i < states.length; i++) {
//        var s = states[i];
//        var find = false;
//        for (var j = 0; j < ss.length; j++) {
//            if (ss[i] == s) {
//                find = true;
//                break;
//            }
//        }
//        if (!find)
//            ss.push(s);
//    }
//    this.renderState();
//};

HeroPanel.prototype.addInnerState = function (key, value) {
    this.d.innerstate[key]=value;
    
    this.renderState();
};

//HeroPanel.prototype.lostStates = function (states) {
//    var ss = this.d.states;
//    for (var i = ss.length - 1; i >= 0; i--) {
//        for (var j = 0; j < states.length; j++) {
//            if (states[j] == ss[i]) {
//                ss.splice(i, 1);
//                break;
//            }
//        }
//    }
//    this.renderState();
//};

HeroPanel.prototype.removeInnerState = function (key) {
    delete this.d.innerstate[key];
    
    this.renderState();
};

HeroPanel.prototype.targets = function (arr) {
    this.d.targets=Array();
    for(var i=0;i<arr.length;i++)
    {
    	this.d.targets.push(arr[i]);
    }
    
    this.renderState();
};

HeroPanel.prototype.renderState = function () {
    var div = this.face.find(".state");
    div.empty();
    var ready = true;
    
    var attack=this.d.attack;
    
    if(this.d.weapon && this.d.weapon.blood>0)
    	attack+=this.d.weapon.attack;
    
    var attackcount=this.d.innerstate["attackcount"];
    var freeze=this.d.innerstate["freeze"];
    
    var states=Array();
    
    //alert(attack+ " "+attackcount);
    if(attack>0 && attackcount>0 && !freeze)
    {
    	ready=true;
    }
    else
    {
    	if(freeze)
    		states[states.length]="冰冻";
    	else
    		states[states.length]="休息";
    	
    	ready=false;
    }
    
        
    for (var i = 0; i < states.length; i++) {
        var s = states[i];
        
        if (s.length > 2) {
            $("<font title='" + s + "'>" + s.substr(0, 2) + "</font>").appendTo(div);
        }
        else {
            $("<font >" + s + "</font>").appendTo(div);
        }
    }
    if (this.self) {
        if (ready && this.field.d.turn)
            this.face.draggable("option","cancel","");
        else 
            this.face.draggable("option","cancel","#"+this.face.attr("id"));
    }
};
HeroPanel.prototype.useSkill = function () {
    var field = this.field;
    if (!field.d.turn)
        return;
    var power = field.d.player.power;
    if (this.d.skill.cost > power) {
        r.error("魔法不足！");
        return;
    }

    var me = this;

    if (this.d.skill.target) {
        
    	var doms=[];
    	for(var i=0;i<this.d.skill.targets.length;i++)
    	{
    		var mh=this.field.action.findmh(this.d.skill.targets[i]);
    		
    		if(mh.id=="g61" || mh.id=="g62")
    			doms.push(mh.face);
    		else	
    			doms.push(mh.dom);
    	}    	
    	
    	targetPanel.show("请选择一个目标", doms, function (tid) {
            server("legend.client.userSkill", tid, function () {
                me.skill.hide();
            });
        });
    }
    else {
        server("legend.client.userSkill", "", function () {
            me.skill.hide();
        });
    }

};
HeroPanel.prototype.showSkill = function () {
    this.skill.show();
}

var PlayerPanel = function (dom, field) {
    this.self = true;
    this.field = field;
    this.dom = $("<div class='playerpanel'/>").appendTo(dom);
    this.minionPanel = new MinionPool(this.dom, true, field);
    this.cardPanel = new CardPool(this.dom, true, field);
    this.heroPanel = new HeroPanel(this.dom, true,field);
    this.powerPanel = new PowerPanel(this.dom);
    this.cardheap = new CardHeap(this.dom);
};
PlayerPanel.prototype.render = function (d) {
    this.d = d;
    this.heroPanel.render(d);
    this.minionPanel.render(d.minions);
    this.cardPanel.render(d.handCards);
    this.powerPanel.render(d.power, d.maxpower);
    this.cardheap.render(d.cardsCount);
};

var EnemyPanel = function (dom, field) {
    this.self = false;
    this.dom = $("<div class='enemypanel'/>").appendTo(dom);
    this.minionPanel = new MinionPool(this.dom,false, field);
    this.cardPanel = new CardPool(this.dom,false);
    this.heroPanel = new HeroPanel(this.dom,false, field);
    this.powerPanel = new PowerPanel(this.dom);
    this.cardheap = new CardHeap(this.dom);
    
    this.field=field;
};
EnemyPanel.prototype.render = function (d) {
    this.d = d;
    this.heroPanel.render(d);
    this.minionPanel.render(d.minions);
    this.cardPanel.render(d.handCards);
    this.powerPanel.render(d.power,d.maxpower);
    this.cardheap.render(d.cardsCount);
};
EnemyPanel.prototype.targetRender = function () {
//    var minions = this.minionPanel.minions;
//    var face = this.heroPanel.face;
//
//    var hascharge = false;
//    for (var i = 0; i < minions.length; i++) {
//        var m = minions[i];
//        for (var j = 0; j < m.d.states.length; j++) {
//            if (m.d.states[j] == "嘲讽") {
//                hascharge = true;
//                break;
//            }
//        }
//    }
//
//    if (!hascharge) {
//        for (var i = 0; i < minions.length; i++) {
//            minions[i].dom.droppable("option", "accept", ".attackhelper");
//        }
//        face.droppable("option", "accept", ".attackhelper");
//    } else {
//        face.droppable("option", "accept", "");
//        for (var i = 0; i < minions.length; i++) {
//            var m = minions[i];
//            m.dom.droppable("option", "accept", "");
//            for (var j = 0; j < m.d.states.length; j++) {
//                if (m.d.states[j] == "嘲讽") {
//                    m.dom.droppable("option", "accept", ".attackhelper");
//                    break;
//                }
//            }
//        }
//    }
};

var TrustCardPanel = function (dom) {
    this.dom = $("<div class='trustcardpanel' />").appendTo(dom);
    this.img = $("<img class='trustcardpanelimg' />").appendTo(dom).hide();

    var me = this;

    this.dom.mousemove(function (e) {
        if (e.target.tagName != "SPAN")
            return;
        me.img.show();
        var t = $(e.target);
        var imageid = t.attr("imageid");
        var src = "http://hearthstone.nos.netease.com/1/cards/" + imageid ;
        if (me.img.attr('src') == src)
            return;
        me.img.attr("src", src);
        
    });
    this.dom.mouseleave(function () {
        me.img.hide();
    });
};
TrustCardPanel.prototype.render = function (cs) {
    this.dom.empty();
    for (var i = cs.length - 1; i >= 0; i--) {
        var c = cs[i];
        $("<span title='" + c.name + "' imageid='"+c.imageid+"' "+(c.self?"class='self'":"")+">" + c.name + "</span>").appendTo(this.dom);
    }
};
TrustCardPanel.prototype.add = function (imageid, name, self) {
    $("<span title='" + name + "' imageid='"+imageid+"' "+(self?"class='self'":"")+">" + name + "</span>").prependTo(this.dom);
};

var Field = function (dom) {
    this.action = new Action(this);
    this.dom = $("<div class='field'/>").hide().appendTo(dom);
    this.area = $("<div class='area'/>").appendTo(this.dom);

    this.selfp = new PlayerPanel(this.dom, this);
    this.enemyp = new EnemyPanel(this.dom, this);
    this.trustcardpanel = new TrustCardPanel(this.dom);

    var me = this;
    this.nextbtn = $("<div class='nextbtn'>结束回合</div>").appendTo(this.dom).click(function () {
        me.next();
    });
    this.surrenderbtn=$("<div class='surrenderbtn'>投降</div>").appendTo(this.dom).click(function () {
        if(confirm("你确定要认输吗？"))
            me.surrender();
    });
    r.noSelect(this.dom);
    this.area.droppable({
        accept: ".card",
        activeClass: "area-hover",
        hoverClass: "area-active",
        drop: function (event, ui) {
            var id = ui.draggable.attr("id");
            
            
            //alert(ui.position.top+","+ ui.position.left);
            //alert(ui.offset.top+","+ui.offset.left);
            
            var index=-1;
                        
            for(var i=0;i<me.selfp.minionPanel.minions.length;i++)
        	{
            	var minion=me.selfp.minionPanel.minions[i];
            	
            	//alert(minion.dom.offset().top+" "+minion.dom.offset().left);
            	
            	if(ui.offset.left<minion.dom.offset().left)
        		{
            		index=i;
            		break;
        		}
        	}
            
            if(index<0)
            	index=me.selfp.minionPanel.minions.length;
            
            //alert(index);
            
            me.selfp.cardPanel.use(id,index);
            
            //ui.draggable.hide();
        }
    });
};
Field.prototype.next = function () {
    if (!this.d)
        return;
    if (!this.d.turn)
        return;

    var me = this;
    this.d.turn = false;
    this.nextbtn.removeClass("turnbtn").text("对方回合");
    server("legend.client.next", function () {
        //me.sync();
    });
};
Field.prototype.surrender = function () {
    if (!this.d)
        return;
    var me = this;
    server("legend.client.surrender", function () {
        //me.sync();
    });
};
Field.prototype.sync = function () {
    var me = this;
    server("legend.client.field", function (d) {
        me.d = d;
        if (!d) {
            me.showWaite();
        }
        else {
            me.hideWaite();
            me.render();
            
        }
    });
};
Field.prototype.render = function () {
    if (!this.d.end) {
        this.selfp.render(this.d.player);
        this.enemyp.render(this.d.enemy);
        this.trustcardpanel.render(this.d.trashCards);

        this.renderNextBtn();
        this.hideWeaponSkill(this.d.turn);
        this.action.start();
    }
    else {
        this.action.showWin(this.d.win);
    }
};
Field.prototype.renderNextBtn = function () {
    if (this.d.turn) {
        this.nextbtn.addClass("turnbtn").text("结束回合");
    }
    else {
        this.nextbtn.removeClass("turnbtn").text("对方回合");
    }
};
Field.prototype.hideWeaponSkill = function (turn) {
    this.enemyp.heroPanel.showSkill();
    this.selfp.heroPanel.showSkill();
    if (turn) {
        this.enemyp.heroPanel.showHeroAttack(false);
        this.selfp.heroPanel.showHeroAttack(true);
        return;
    }
    else
    {
	    this.enemyp.heroPanel.showHeroAttack(true);
	    this.selfp.heroPanel.showHeroAttack(false);
    }
};
Field.prototype.showWaite = function () {
    if (!this.waitepanel) {
        this.waitepanel = $("<div class='waitepanel'/>").appendTo($("body"));
        var div=$("<div>目前没有正在进行的战局，回去找美女炉石老板去...</div>").appendTo(this.waitepanel);
        $("<a href='index.jsp' class='btn'>返回</a>").appendTo(div);
    }
    this.waitepanel.show();
    document.title = "等待中";
    var me = this;
    setTimeout(function () {
        me.sync();
    }, 3000)
};
Field.prototype.hideWaite = function () {
    if (this.waitepanel)
        this.waitepanel.hide();
    this.dom.show();
    document.title = "已匹配";
};

var Action = function (field) {
    this.field = field;
    this.connectState = false;
    this.actions = [];
    this.end = false;
};


Action.ROUND_CHANGE = 1;
Action.POWER_CHANGE = 2;
Action.MINION_ATTACK_CHANGE = 3;
Action.MINION_BLOOD_CHANGE = 4;
Action.MINION_MAXBLOOD_CHANGE = 5;
Action.MINION_INDEX_CHANGE=6;
Action.MINION_INNERSTATE_ADD=7;
Action.MINION_INNERSTATE_LOST=8;
Action.MINION_OUTERSTATE_ADD=9;
Action.MINION_OUTERSTATE_LOST=10;
Action.MINION_TARGETS_CHANGE=11;
Action.MINION_BORN = 12;
Action.MINION_DIE = 13;
Action.HEROMINION_ATTACK_CHANGE = 14;
Action.HEROMINION_BLOOD_CHANGE = 15;
Action.HEROMINION_DIE = 32;
Action.HEROMINION_GUARD_CHANGE = 31;
Action.HEROMINION_INNERSTATE_ADD=16;
Action.HEROMINION_INNERSTATE_LOST=17;
Action.HEROMINION_TARGETS_CHANGE=18;
Action.GAMECARD_OUTERSTATE_ADD=19;
Action.GAMECARD_OUTERSTATE_LOST=20;
Action.GAMECARD_TARGETS_CHANGE=21;
Action.GAMECARD_NEW = 22;
Action.GAMECARD_LOST = 23;
Action.GAMECARD_USABLE = 30;
Action.WEAPON_ATTACK_CHANGE = 24;
Action.WEAPON_BLOOD_CHANGE = 25;
Action.WEAPON_INNERSTATE_ADD=26;
Action.WEAPON_INNERSTATE_LOST=27;
Action.WEAPON_OUTERSTATE_ADD=28;
Action.WEAPON_OUTERSTATE_LOST=29;
Action.WIN = 33;

//Action.HERO_GUARD_HURT=10;
//Action.HERO_GUARD_CURE=11;
//Action.HERO_DIE = 12;
//Action.HERO_ATTACK = 13;



//Action.CARD_COST_CHANGE = 18;
//Action.WEAPON_ADD = 19;
//Action.WEAPON_CHANGE = 20;
//Action.WEAPON_REMOVE = 21;
//Action.CARD_CHOOSE = 100;

Action.prototype.startSecondClock = function () {


    var me = this;
    me.leftSecond = 45;
    me.selfoper = me.field.d.turn;
    this.clock = setInterval(function () {
        if (!me.leftSecond || me.leftSecond < 0)
            return;
        me.leftSecond--;
        if (me.leftSecond == 0 && me.selfoper) {
            //$(".nextbtn").click();
        };
        document.title = (me.selfoper ? "我方操作" : "对方回合") + " - " + me.leftSecond + "秒";

    }, 1000);
};
Action.prototype.showWin = function (win) {
    var rw = $("<div class='round_win' ></div>").appendTo($("body"));
    var round_win = $("<div class='round_win_panel' ></div>").appendTo(rw);
    if (win) {
        $("<img src='http://img.mm-girl.com/qqbiaoqing/xingmiaobaobao/18.gif' />").appendTo(round_win);
    }
    else {
        $("<img src='http://img.mm-girl.com/qqbiaoqing/xingmiaobaobao/9.gif' />").appendTo(round_win);
    }
    $("<div  class='round_title round_title_" + (win ? "win" : "lost") + "'>你" + (win ? "赢" : "输") + "了</div>").appendTo(round_win);
    $("<a href='end.jsp' class='btn'>结束</a>").appendTo(round_win);
    if (win) {
        $("<span>奖励: "+(this.field.d.winMoney)+"金币</span>").appendTo(round_win);
    }
    this.end = true;
    this.field.nextbtn.remove();
    Action.noOperHover.show();
    this.end = true;
};
Action.prototype.start = function () {
    if (this.connectState)
        return;
    this.connectState = true;
    this._connect();

    this.startSecondClock();
}
Action.prototype._connect = function () {
    if (this.end)
        return;
    var me = this;
    server("legend.client.connect", function (actions) {
        if (actions) {
        	me.doAction(actions);
        };
                
        me._connect();
    }, function () {
        if (!me.errortime)
            me.errortime = 0;
        me.errortime++;
        setTimeout(function () {
            me._connect();
        }, me.errortime * 1000)

    });
};
Action.prototype.doAction = function (actions) {

    for (var i = 0; i < actions.length; i++) {
        this.actions.push(actions[i]);
    }

    var me = this;
    var i = -1;
    var cb = function () {
        i++;
        if (i >= actions.length)
            return;
        var action = actions[i];
        var func = me["action_" + action.type];

        if (func) {
            var parms = [];
            for (var key in action) {
                if (key != "type")
                    parms.push(action[key]);
            }
            parms.push(cb);
            func.apply(me, parms);
        }
        else {
            cb();
        }
    };

    cb();
};
Action.prototype.findmh = function (id) {
    if (this.field.selfp.heroPanel.id == id)
        return this.field.selfp.heroPanel;
    if (this.field.enemyp.heroPanel.id == id)
        return this.field.enemyp.heroPanel;
    var list = this.field.selfp.minionPanel.minions;
    for (var i = 0; i < list.length; i++)
        if (list[i].id == id)
            return list[i];
    list = this.field.enemyp.minionPanel.minions;
    for (var i = 0; i < list.length; i++)
        if (list[i].id == id)
            return list[i];
    return null;
};


Action.prototype.findplayer = function (id) {
    if (this.field.selfp.heroPanel.id == id)
        return this.field.selfp;
    if (this.field.enemyp.heroPanel.id == id)
        return this.field.enemyp;
    return null;
};

Action.prototype["action_" + Action.ROUND_CHANGE] = function (pid, cb) {
    var player = this.findplayer(pid);
    this.field.d.turn = player.self;
    this.field.renderNextBtn();
    if (player.self) {
        this.field.enemyp.targetRender();
        var title = $("<div  class='round_title'>你的回合</div>").appendTo($("body"));
        setTimeout(function () {
            title.remove();
        }, 1000);
    }


    //禁止操作
    if (!Action.noOperHover) {
        Action.noOperHover = $("<div class='no_oper_hover'/>").appendTo($("body"));
    }
    this.leftSecond = 45;
    if (player.self) {
        Action.noOperHover.hide();
        this.selfoper = true;
        document.title = "我方操作";
    }
    else {
        Action.noOperHover.show();
        this.selfoper = false;
        document.title = "对方回合";
    }


    this.field.hideWeaponSkill(player.self);

    cb();
};

Action.prototype["action_" + Action.POWER_CHANGE] = function (pid, power, maxpower, cb) {

    var player = this.findplayer(pid);
    var panel = player.powerPanel;
    panel.render(power, maxpower);
    player.d.power = power;
    player.d.maxpower = maxpower;
    cb();
};

//Action.prototype["action_" + Action.MINION_HURT] = function (id, count, cb) {
//    var mh = this.findmh(id);
//    mh.hurt(count, function(){});
//    cb();
//};
//Action.prototype["action_" + Action.MINION_CURE] = function (id, count, cb) {
//    var mh = this.findmh(id);
//    mh.cure(count, function () { });
//    cb();
//};

Action.prototype["action_" + Action.MINION_ATTACK_CHANGE] = function (id, count, cb) {
    var mh = this.findmh(id);
    mh.changeAttack(count);
    cb();
};

Action.prototype["action_" + Action.MINION_BLOOD_CHANGE] = function (id, count, cb) {
    var mh = this.findmh(id);
    mh.blood(count, function(){});
    cb();
};

Action.prototype["action_" + Action.MINION_MAXBLOOD_CHANGE] = function (id, count, cb) {
    var mh = this.findmh(id);
    mh.maxblood(count, function(){});
    cb();
};

Action.prototype["action_" + Action.MINION_INDEX_CHANGE] = function (id, count, cb) {
    var mh = this.findmh(id);
    mh.index(count, function(){});
    cb();
};

Action.prototype["action_" + Action.MINION_INNERSTATE_ADD] = function (id, key, value, cb) {
	var mh = this.findmh(id);
    mh.addInnerState(key, value);
    cb();
};

Action.prototype["action_" + Action.MINION_INNERSTATE_LOST] = function (id, key,  cb) {
	var mh = this.findmh(id);
    mh.removeInnerState(key);
    cb();
};

Action.prototype["action_" + Action.MINION_OUTERSTATE_ADD] = function (id, sid, key, value, cb) {
	var mh = this.findmh(id);
    mh.addOuterState(sid, key, value);
    cb();
};

Action.prototype["action_" + Action.MINION_OUTERSTATE_LOST] = function (id, sid, cb) {
	var mh = this.findmh(id);
    mh.removeOuterState(sid);
    cb();
};

Action.prototype["action_" + Action.MINION_TARGETS_CHANGE] = function (id, targets, cb) {
	var mh = this.findmh(id);
    mh.targets(targets);
    cb();
};

Action.prototype["action_" + Action.MINION_BORN] = function (pid, minion, cb) {
    var player = this.findplayer(pid);
    var mpanel = player.minionPanel;
    mpanel.add(minion);
    setTimeout(function () {
        cb();
    },20*movies.timeStep);
};
Action.prototype["action_" + Action.MINION_DIE] = function (id, cb) {
    var mh = this.findmh(id);
    var me = this;
    mh.die(function () {
        if (!mh.self) {
            me.field.enemyp.targetRender();
        }
        //cb();
    });
    cb();
};

Action.prototype["action_" + Action.HEROMINION_ATTACK_CHANGE] = function (id, count, cb) {
    var mh = this.findmh(id);
    mh.changeAttack(count);
    cb();
};

Action.prototype["action_" + Action.HEROMINION_BLOOD_CHANGE] = function (id, count, cb) {
    var mh = this.findmh(id);
    mh.blood(count, function(){});
    cb();
};

Action.prototype["action_" + Action.HEROMINION_DIE] = function (id, cb) {
	var mh = this.findmh(id);
	mh.die(cb);
};

//Action.prototype["action_" + Action.HERO_GUARD_HURT] = function (id, count, cb) {
//    var mh = this.findmh(id);
//    mh.guard_hurt(count, function () { });
//    cb();
//};
//Action.prototype["action_" + Action.HERO_GUARD_CURE] = function (id, count, cb) {
//    var mh = this.findmh(id);
//    mh.guard_cure(count, function(){});
//    cb();
//};

Action.prototype["action_" + Action.HEROMINION_GUARD_CHANGE] = function (id, count, cb) {
	var mh = this.findmh(id);
	mh.guard(count, function () { });
	cb();
};

Action.prototype["action_" + Action.HEROMINION_INNERSTATE_ADD] = function (id, key, value, cb) {
	var mh = this.findmh(id);
    mh.addInnerState(key, value);
    cb();
};

Action.prototype["action_" + Action.HEROMINION_INNERSTATE_LOST] = function (id, key,  cb) {
	var mh = this.findmh(id);
    mh.removeInnerState(key, value);
    cb();
};

Action.prototype["action_" + Action.HEROMINION_TARGETS_CHANGE] = function (id, targets, cb) {
	var mh = this.findmh(id);
    mh.targets(targets);
    cb();
};

Action.prototype["action_" + Action.GAMECARD_OUTERSTATE_ADD] = function (pid, id,  sid, key, value, cb) {
	var player = this.findplayer(pid);
    var cpanel = player.cardPanel;
    var gameCard=cpanel.findCard(id);
    
    gameCard.addOuterState(sid, key, value);
};

Action.prototype["action_" + Action.GAMECARD_OUTERSTATE_LOST] = function (pid, id, sid, cb) {
	var player = this.findplayer(pid);
    var cpanel = player.cardPanel;
    var gameCard=cpanel.findCard(id);
    
    gameCard.removeOuterState(sid);
};

Action.prototype["action_" + Action.GAMECARD_TARGETS_CHANGE] = function (pid, id, targets, cb) {
	var player = this.findplayer(pid);
    var cpanel = player.cardPanel;
    var gameCard=cpanel.findCard(id);
    
    gameCard.targets(targets);
    cb();
};

Action.prototype["action_" + Action.GAMECARD_NEW] = function (pid, gamecard, cb) {
    var player = this.findplayer(pid);
    if (player.cardheap.count > 0) {
        player.cardheap.render(player.cardheap.count - 1);
    }
    var cpanel = player.cardPanel;
    cpanel.add(gamecard, cb);
};

Action.prototype["action_" + Action.GAMECARD_LOST] = function ( pid, id, cb) {
    var player = this.findplayer(pid);
    var cpanel = player.cardPanel;
    cpanel.lost(id, cb);
};

Action.prototype["action_" + Action.GAMECARD_USABLE] = function (pid, id, usable, cb) {
    var player = this.findplayer(pid);
    var cpanel = player.cardPanel;
    var gameCard=cpanel.findCard(id);
    
    gameCard.usable(usable);
};

Action.prototype["action_" + Action.WEAPON_ATTACK_CHANGE] = function (pid, attack, cb) {
    var player = this.findplayer(pid);
    var heropanel = player.heroPanel;
    heropanel.changeweaponattack(attack);
    cb();
};

Action.prototype["action_" + Action.WEAPON_BLOOD_CHANGE] = function (pid, blood, cb) {
    var player = this.findplayer(pid);
    var heropanel = player.heroPanel;
    heropanel.changeweaponblood(blood);
    cb();
};

Action.prototype["action_" + Action.WEAPON_INNERSTATE_ADD] = function (pid, key, value, cb) {
	var player = this.findplayer(pid);
    var heropanel = player.heroPanel;
    heropanel.addweaponinnerstate(key, value);
    cb();
};

Action.prototype["action_" + Action.WEAPON_INNERSTATE_LOST] = function (pid, key, cb) {
	var player = this.findplayer(pid);
    var heropanel = player.heroPanel;
    heropanel.removeweaponinnerstate(key);
    cb();
};

Action.prototype["action_" + Action.WEAPON_OUTERSTATE_ADD] = function (pid, sid, key, value, cb) {
	var player = this.findplayer(pid);
    var heropanel = player.heroPanel;
    heropanel.addweaponouterstate(sid, key, value);
    cb();
};

Action.prototype["action_" + Action.WEAPON_OUTERSTATE_LOST] = function (pid, sid, cb) {
	var player = this.findplayer(pid);
    var heropanel = player.heroPanel;
    heropanel.removeweaponouterstate(sid);
    cb();
};


Action.prototype["action_" + Action.WIN] = function (pid, cb) {
	var player = this.findplayer(pid);
	this.showWin(player.self);
	cb();
};







//Action.prototype["action_" + Action.MINION_ATTACK] = function (id, tid, cb) {
//    setTimeout(function () {
//        cb();
//    }, 20 * movies.timeStep);
//};
//Action.prototype["action_" + Action.MINION_STATE_ADD] = function (id, states, cb) {
//    var mh = this.findmh(id);
//    mh.addStates(states);
//    cb();
//};
//Action.prototype["action_" + Action.MINION_STATE_LOST] = function (id, states, cb) {
//    var mh = this.findmh(id);
//    mh.lostStates(states);
//    cb();
//};
//
//Action.prototype["action_" + Action.CARD_USE] = function (pid, cid, name, imageid, cb) {
//    var player = this.findplayer(pid);
//
//    this.field.trustcardpanel.add(imageid, name, player.self);
//    var img = $("<img class='cardshow' src='http://hearthstone.nos.netease.com/1/cards/" + imageid + "'/>").appendTo(this.field.dom);
//    setTimeout(function () {
//        cb();
//    }, 1000)
//    setTimeout(function () {
//        img.remove();
//    }, 3000)
//};
//

//Action.prototype["action_" + Action.HERO_ATTACK] = function (id, tid, cb) {
//    setTimeout(function () {
//        cb();
//    }, 20 * movies.timeStep);
//};
//
//

//
//Action.prototype["action_" + Action.HEROMINION_ATTACK_CHANGE] = function (id, count, cb) {
//    var mh = this.findmh(id);
//    mh.changeAttack(count);
//    cb();
//};
//
//Action.prototype["action_" + Action.CARD_COST_CHANGE] = function (id, count, cb) {
//    cb();
//};
//Action.prototype["action_" + Action.WEAPON_ADD] = function (pid, name, attack, blood, cb) {
//    var player = this.findplayer(pid);
//    var heropanel = player.heroPanel;
//    heropanel.addweapon(name, attack, blood);
//    cb();
//};
//Action.prototype["action_" + Action.WEAPON_CHANGE] = function (pid, attack,blood, cb) {
//    var player = this.findplayer(pid);
//    var heropanel = player.heroPanel;
//    heropanel.changeweapon(attack, blood);
//    cb();
//};
//Action.prototype["action_" + Action.WEAPON_REMOVE] = function (pid, cb) {
//    var player = this.findplayer(pid);
//    var heropanel = player.heroPanel;
//    heropanel.removeweapon();
//    cb();
//};
//Action.prototype["action_" + Action.CARD_CHOOSE] = function (cs, count, cb) {
//    this.choosepanel = $("<div/>").addClass("choosepanel").appendTo(this.field.dom);
//    $("<h1>请选择一张牌：</h1>").appendTo(this.choosepanel);
//    var d = $("<div class='cardpanel'/>").css("width",cs.length*105).appendTo(this.choosepanel);
//    var me = this;
//    for (var i = 0; i < cs.length; i++) {
//        var n = cs[i];
//        var card = new Card(d, null);
//        card.render(r.guid(), n, true);
//        card.dom.attr("name", n);
//        card.dom.click(function () {
//            var cardname = $(this).attr("name");
//            server("legend.client.setChoosedCards", cardname, function () {
//                if (me.choosepanel) {
//                    me.choosepanel.remove();
//                    cb();
//                };
//            });
//        })
//    };
//};
$(function () {

    window.field = new Field($("body"));
    window.field.sync();

});