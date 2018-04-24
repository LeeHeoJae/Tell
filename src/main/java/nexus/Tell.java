package nexus;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;
import cn.nukkit.form.window.*;
import cn.nukkit.form.response.*;
import cn.nukkit.form.element.*;

import nexus.form.TellForm;

import java.util.ArrayList;

public class Tell extends PluginBase implements Listener{
	@Override
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this,this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
		if(sender instanceof Player){
			Player player=(Player) sender;
			ArrayList<String> playerList=new ArrayList<String>();
			for(Player p:this.getServer().getOnlinePlayers().values()){
				playerList.add(p.getName());
			}
			TellForm window=new TellForm();
			ElementDropdown dropDown=new ElementDropdown("대상",playerList);
			ElementInput input=new ElementInput("메세지","메세지를 입력해주세요");
			
			window.addElement(dropDown);
			window.addElement(input);
			player.showFormWindow(window);
		}
		return true;
	}
	
	@EventHandler
	public void onSend(PlayerFormRespondedEvent ev){
		if(ev.getWindow() instanceof TellForm){
			if(ev.getResponse() instanceof FormResponseCustom){
				FormResponseCustom result=(FormResponseCustom)ev.getResponse();
				FormResponseData dropDownData=result.getDropdownResponse(0);
				String dropDown=dropDownData.getElementContent();
				String input=result.getInputResponse(1);
				Player target=this.getServer().getPlayer(dropDown);
				if(target==null){
					ev.getPlayer().showFormWindow(new FormWindowSimple("Sorry","대상이 잘못되었습니다."));
					return;
				}
				if(input==null||input.isEmpty()){
					ev.getPlayer().showFormWindow(new FormWindowSimple("Sorry","메세지를 입력해주세요."));
					return;
				}
				target.sendMessage("§8"+ev.getPlayer().getName()+" : "+input);
			}
		}
	}
}