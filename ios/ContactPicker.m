#import <Foundation/Foundation.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import <UIKit/UIKit.h>
#import <AddressBook/AddressBook.h>
#import <AddressBookUI/AddressBookUI.h>


#import "RCTBridgeModule.h"
#import "RCTRootView.h"
#import "RCTLog.h"


@interface ContactPicker : NSObject <RCTBridgeModule, ABPeoplePickerNavigationControllerDelegate,UINavigationControllerDelegate>
@property (nonatomic, strong) UIViewController *root;
@property (nonatomic, strong) ABPeoplePickerNavigationController *pickerController;
@property (nonatomic, strong) RCTPromiseResolveBlock resolve;
@property (nonatomic, strong) RCTPromiseRejectBlock reject;
@end

@implementation ContactPicker

RCT_EXPORT_MODULE();


@synthesize bridge = _bridge;



- (instancetype)init {
    self.root = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    while (self.root.presentedViewController != nil) {
        NSLog(@"Looking for root");
        
        self.root = self.root.presentedViewController;
    }
    self.pickerController = [[ABPeoplePickerNavigationController alloc] init];
    return self;
}


RCT_REMAP_METHOD(pickContact,resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    self.resolve = resolve;
    self.reject = reject;
    self.pickerController.peoplePickerDelegate = self;
    [self.root presentViewController:self.pickerController animated:YES completion:nil];
    
    
}
- (void)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker
                         didSelectPerson:(ABRecordRef)person {
    NSArray* emails = (__bridge_transfer NSArray *)ABRecordCopyValue(person, kABPersonEmailProperty);
    [self.root dismissViewControllerAnimated:YES completion:nil];
    self.resolve(emails);
}

@end