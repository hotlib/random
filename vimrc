execute pathogen#infect()
syntax on
set number
filetype on
filetype plugin indent on
let g:haskell_indent_if = 3
let g:haskell_indent_case = 5
let g:haskell_indent_let = 4
let g:haskell_indent_where = 6
let g:haskell_indent_do = 3
let g:haskell_indent_in = 1

set statusline+=%#warningmsg#
set statusline+=%{SyntasticStatuslineFlag()}
set statusline+=%*

let g:syntastic_always_populate_loc_list = 1
let g:syntastic_auto_loc_list = 1
let g:syntastic_check_on_open = 0 
let g:syntastic_check_on_wq = 0
au FileType haskell nnoremap <buffer> <F1> :HdevtoolsType<CR>
au FileType haskell nnoremap <buffer> <silent> <F2> :HdevtoolsClear<CR>
au FileType haskell nnoremap <buffer> <silent> <F3> :HdevtoolsInfo<CR>

map <silent> tu :call GHC_BrowseAll()<CR>
map <silent> tw :call GHC_ShowType(1)<CR>

map <Tab> :ToggleBufExplorer<CR>
map <F6> :NERDTreeToggle<CR>

nnoremap <C-S> i
nnoremap <C-A> a
inoremap <C-S> <c-o>:w<cr><Esc>l 
set laststatus=2

let g:ctrlp_user_command = 'find %s -type f'
let g:ctrlp_map = '<c-f>'
let g:ctrlp_cmd = 'CtrlP'

set tabstop=8 softtabstop=0 expandtab shiftwidth=4 smarttab

