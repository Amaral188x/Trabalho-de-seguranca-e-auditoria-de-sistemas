const fs=require('node:fs');
const assert=require('node:assert/strict');
const {chromium}=require('C:/Users/Lenovo/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/playwright');
(async()=>{
  const browser=await chromium.launch({channel:'msedge',headless:true});
  const page=await browser.newPage(), results=[];
  for(const width of [1920,1440,1024,390,320]){
    await page.setViewportSize({width,height:1000});
    await page.goto('file:///'+process.cwd().replaceAll('\\','/')+'/index.html',{waitUntil:'networkidle'});
    await page.evaluate(()=>document.fonts.ready);
    const geometry=()=>page.locator('body *').evaluateAll(nodes=>nodes.map(e=>{const r=e.getBoundingClientRect();return {x:r.x,y:r.y,width:r.width,height:r.height}}));
    const old=await page.addStyleTag({content:'.xfo62xy.x21rrya::before {content:none !important;}'});
    const before=await geometry();await page.screenshot({path:`validation/divider-before-${width}.png`,fullPage:true});
    await old.evaluate(e=>e.remove());assert.deepEqual(await geometry(),before);
    const result=await page.locator('.xfo62xy.x21rrya').evaluate(e=>{const r=e.getBoundingClientRect(),c=getComputedStyle(e),p=getComputedStyle(e,'::before');return {viewport:innerWidth,x:r.x,width:r.width,height:r.height,paintedTop:r.top+parseFloat(p.top),background:c.backgroundColor,extensionBackground:p.backgroundColor,display:c.display,pointerEvents:p.pointerEvents};});
    if(result.width>0){assert.equal(result.paintedTop,0);assert.equal(result.width,2);assert.equal(result.background,'rgb(75, 77, 79)');assert.equal(result.extensionBackground,result.background);}
    assert.equal(result.pointerEvents,'none');results.push(result);
    await page.screenshot({path:`validation/divider-after-${width}.png`,fullPage:true});
  }
  fs.writeFileSync('validation/divider-results.json',JSON.stringify(results,null,2));await browser.close();
  console.log('PASS: divider extends to y=0 with original color/width; unchanged geometry; 5 resolutions.');
})().catch(e=>{console.error(e);process.exit(1)});
