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
    await page.mouse.move(0,0);
    const geometry=()=>page.locator('body *').evaluateAll(nodes=>nodes.map(e=>{const r=e.getBoundingClientRect();return {x:r.x,y:r.y,width:r.width,height:r.height}}));
    const previous=await page.addStyleTag({content:'.x2bj2ny:has(#login_form)::before {content:none !important;}'});
    const before=await geometry();
    await page.screenshot({path:`validation/panel-top-before-${width}.png`,fullPage:true});
    await previous.evaluate(e=>e.remove());
    assert.deepEqual(await geometry(),before);
    const details=await page.locator('.x2bj2ny:has(#login_form)').evaluate(panel=>{
      const r=panel.getBoundingClientRect(), p=getComputedStyle(panel,'::before');
      return {width:innerWidth,panelLeft:r.left,panelRight:r.right,paintedTop:r.top+parseFloat(p.top),stripHeight:parseFloat(p.height),background:p.backgroundColor,panelBackground:getComputedStyle(panel).backgroundColor,pointerEvents:p.pointerEvents};
    });
    assert.equal(details.paintedTop,0);assert.equal(details.stripHeight,18);assert.equal(details.background,details.panelBackground);assert.equal(details.pointerEvents,'none');
    await page.screenshot({path:`validation/panel-top-after-${width}.png`,fullPage:true});
    results.push(details);
  }
  fs.writeFileSync('validation/panel-top-results.json',JSON.stringify(results,null,2));
  await browser.close();console.log('PASS: login background reaches y=0; every element keeps its geometry; 5 viewports.');
})().catch(e=>{console.error(e);process.exit(1)});
