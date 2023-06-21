import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxSecurityLevel;

public class App {
    public static void main(String[] args) {
        JSGFPLib jsgfpLib = new JSGFPLib();

        // Initialize the SecuGen SDK
        long initError = jsgfpLib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if (initError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to initialize SecuGen SDK.");
            return;
        } else {
            System.out.println("SecuGen SDK initialized successfully.");
        }

        // Open the fingerprint reader
        long openError = jsgfpLib.OpenDevice(SGFDxDeviceName.SG_DEV_AUTO);
        if (openError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to open fingerprint reader.");
            return;
        } else {
            System.out.println("Fingerprint reader opened successfully.");
        }

        // Get device information
        SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
        long infoError = jsgfpLib.GetDeviceInfo(deviceInfo);
        if (infoError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to retrieve device information.");
            return;
        } else {
            System.out.println("Device Information:");
            System.out.println("  Device ID: " + deviceInfo.deviceID);
            System.out.println("  Image Width: " + deviceInfo.imageWidth);
            System.out.println("  Image Height: " + deviceInfo.imageHeight);
        }

        // Capture a fingerprint image
        byte[] imageBuffer = new byte[deviceInfo.imageWidth * deviceInfo.imageHeight];
        long captureError = jsgfpLib.GetImage(imageBuffer);
        if (captureError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to capture fingerprint image.");
            return;
        } else {
            System.out.println("Fingerprint image captured successfully.");
        }

        // Get the maximum template size
        int[] maxTemplateSize = new int[1];
        long maxSizeError = jsgfpLib.GetMaxTemplateSize(maxTemplateSize);
        if (maxSizeError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to retrieve maximum template size.");
            return;
        } else {
            System.out.println("Maximum template size retrieved successfully.");
        }

        // Create the template buffer
        byte[] templateBuffer = new byte[maxTemplateSize[0]];

        //Image Quality
        int[] imageQuality = new int[1];
        long captureQualityError = jsgfpLib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer, imageQuality);
        

        // Set information about the template
        SGFingerInfo fingerInfo = new SGFingerInfo();
        fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
        fingerInfo.ImageQuality = imageQuality[0]; 
        fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
        fingerInfo.ViewNumber = 1;

        // Create template from the captured image
        long createTemplateError = jsgfpLib.CreateTemplate(fingerInfo, imageBuffer, templateBuffer);
        if (createTemplateError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to create fingerprint template.");
            return;
        } else {
            System.out.println("Fingerprint template created successfully.");
        }

        // Get the actual template size
        int[] templateSize = new int[1];
        long templateSizeError = jsgfpLib.GetTemplateSize(templateBuffer, templateSize);
        if (templateSizeError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to retrieve template size.");
            return;
        } else {
            System.out.println("Template size retrieved successfully.");
            System.out.println("ImageQuality: " + imageQuality[0]);
        }


        //Matching Templates
        long sl = SGFDxSecurityLevel.SL_ABOVE_NORMAL; 
        boolean[] matched = new boolean[1];
        // Match the templates
        long matchError = jsgfpLib.MatchTemplate(templateBuffer, templateBuffer, sl, matched);
        if (matchError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
        System.out.println("Failed to match fingerprint templates.");
        return;
        }else{
             System.out.println(matched[0]);
        }


        
    }
}
