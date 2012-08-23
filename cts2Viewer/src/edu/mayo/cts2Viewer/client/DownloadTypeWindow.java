package edu.mayo.cts2Viewer.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Window to select the download format
 */
public class DownloadTypeWindow extends Window {

	public DownloadTypeWindow() {
	    super();

	    setWidth(250);  
        setHeight(160);  
        setTitle("Download Format");  
        setShowMinimizeButton(false);  
        
        setIsModal(true);  
        setShowModalMask(true);  
        setModalMaskOpacity(60);
        centerInPage();  

        setAnimateShowTime(1000);
        setAnimateFadeTime(1000);
        setAnimateShowEffect(AnimationEffect.WIPE);
        
        addItem(createWindowTitle("Select a Download Format"));
	    addItem(createDownloadOptions());
    }

	private Label createWindowTitle(String title) {
		
		String titleFormatted = "<b style=\"color: #6f6f6f;font-family: Arial,Helvetica,sans-serif;font-size:18px;font-weight:bold;text-decoration:none\">"
		        + title + "</b>";
		
		Label windowTitleLabel = new Label(titleFormatted);
		windowTitleLabel.setWidth100();
		windowTitleLabel.setHeight(25);
		windowTitleLabel.setAlign(Alignment.CENTER);
		windowTitleLabel.setValign(VerticalAlignment.TOP);
		windowTitleLabel.setWrap(false);
		
		return windowTitleLabel;
	}
	
	private HLayout createDownloadOptions() {
	    HLayout optionsLayout = new HLayout();
	    optionsLayout.setHeight(60);
	    optionsLayout.setWidth100();
	    optionsLayout.setAlign(Alignment.CENTER);
	    
	    VLayout excelLayout = createOptionLayout("Excel", "download_excel_48.png");
	    VLayout xmlLayout = createOptionLayout("XML", "download_xml_48.png");
	    
	    optionsLayout.addMember(excelLayout);
	    optionsLayout.addMember(xmlLayout);
	    
	    return optionsLayout;
    }

	private VLayout createOptionLayout(String title, String imgName) {
		
		final VLayout layout = new VLayout();
		layout.setHeight(70);
		layout.setWidth(80);
		layout.setBackgroundColor("#ffffff");
		layout.setMargin(15);
		layout.setMembersMargin(5);
		layout.setEdgeBackgroundColor("gray");
		layout.setEdgeSize(1);
		layout.setShowEdges(true);
		
		layout.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				layout.setBackgroundColor("#ededed");
				
			}
		});
		
		layout.addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				layout.setBackgroundColor("#ffffff");
				
			}
		});
		
		
		Label titleLabel = new Label("<b>" + title + "</b>");
		titleLabel.setWidth(48);
		titleLabel.setHeight(20);
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setValign(VerticalAlignment.TOP);
		titleLabel.setWrap(false);
//		label.setEdgeBackgroundColor("#ffffff");
//		label.setContents("title");
		
		Img downloadImg = new Img(imgName, 48, 48);  
		downloadImg.setWidth(48);
		downloadImg.setHeight(48);
		
		downloadImg.setImageType(ImageStyle.NORMAL);  
		//downloadImg.setBorder("1px dashed gray");  
				
		layout.addMember(titleLabel);
		layout.addMember(downloadImg);
		
		return layout;
	}

	
	
}
