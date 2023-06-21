import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;

public class App {
    public static void main(String[] args) {
        JSGFPLib jsgfpLib = new JSGFPLib();

        // Initialize the SecuGen SDK
        long initError = jsgfpLib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if (initError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to initialize SecuGen SDK.");
            return;
        }

        // Open the fingerprint reader
        long openError = jsgfpLib.OpenDevice(SGFDxDeviceName.SG_DEV_AUTO);
        if (openError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to open fingerprint reader.");
            return;
        }

        // Get device information
        SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
        long infoError = jsgfpLib.GetDeviceInfo(deviceInfo);
        if (infoError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to retrieve device information.");
            return;
        }

        // Capture a fingerprint image
        byte[] imageBuffer = new byte[deviceInfo.imageWidth * deviceInfo.imageHeight];
        long captureError = jsgfpLib.GetImage(imageBuffer);
        if (captureError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to capture fingerprint image.");
            return;
        }

        // Get the maximum template size
        int[] maxTemplateSize = new int[1];
        long maxSizeError = jsgfpLib.GetMaxTemplateSize(maxTemplateSize);
        if (maxSizeError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to retrieve maximum template size.");
            return;
        }

        // Create the template buffer
        byte[] templateBuffer = new byte[maxTemplateSize[0]];

        // Set information about the template
        SGFingerInfo fingerInfo = new SGFingerInfo();
        fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
        fingerInfo.ImageQuality = 80; 
        fingerInfo.ImpressionType =  SGImpressionType.SG_IMPTYPE_LP;
        fingerInfo.ViewNumber = 1;

        // Create template from the captured image
        long createTemplateError = jsgfpLib.CreateTemplate(fingerInfo, imageBuffer, templateBuffer);
        if (createTemplateError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to create fingerprint template.");
            return;
        }

        // Get the actual template size
        int[] templateSize = new int[1];
        long templateSizeError = jsgfpLib.GetTemplateSize(templateBuffer, templateSize);
        if (templateSizeError != SGFDxErrorCode.SGFDX_ERROR_NONE) {
            System.out.println("Failed to retrieve template size.");
            return;
        }

    }
    }
