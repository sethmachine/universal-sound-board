<!-- Thank you for taking the time to make a PR to improve this repo!
This template outlines a series of steps you should take to make sure your change doesn't break the latest build, and also how you should document your changes.

Before merging, make sure the prettier-maven-plugin has been run by building locally: https://github.com/HubSpot/prettier-maven-plugin

The issue should link to an existing GitHub issue, or documentation of what is being addressed by the PR.  

-->
### Description

Issue:

<!-- Describe the purpose of this PR.  What changes are you making and why.
List each change as separate bullet, ideally starting with a present tense action verb ("add", "create", "abstract", "simplify", etc.)
-->

Changes in this PR:
* <!--Please list changes here -->


### Manual Tests

<!-- Until a full suite of unit, acceptance, and integration tests are written, 
all changes need to be validated manually.  
These manual tests are not exhaustive but make sure core functionality is not broken.
-->

- [ ] `mvn clean verify` builds successfully and prettifies all Java code
- [ ] able to create a sink audio mixer for a physical microphone and wire it to a virtual audio device
- [ ] verify that audio to physical microphone is successfully sent to virtual audio device (test via Discord, etc.)
- [ ] able to play a supported audio file to a physical speaker (you can hear the clip played without distortion, etc.)
- [ ] able to play a supported audio file to a sink and source simultaneously

Were all manual tests verified on supported operating systems?  

- [ ] Verified on macOS
- [ ] Verified on Windows

<!--
If applicable
### Documentation

- [ ] Update the README.md to document any new changes, behaviors, or improvements from your PR.
-->