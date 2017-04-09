<?php

namespace app\Core\Domain\Contract;


interface HasId
{
    public function getId() : string;
}